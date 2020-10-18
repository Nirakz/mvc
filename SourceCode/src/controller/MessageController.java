package controller;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.continuation.Continuation;

import org.eclipse.jetty.continuation.ContinuationSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.vng.jcore.profiler.ProfilerLog;

import dao.AccountDAO;
import dao.MessageDAO;

import executor.AliveChecker;
import executor.MessageStore;

import pojo.LinkDescriptor;
import pojo.Message;
import pojo.Notification;
import util.LinkParser;
import util.MyLogger;
import util.ValueHelper;

/**
 * Servlet implementation class MessageController
 */

public class MessageController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Gson gson = new Gson();

	private Map<String, LinkedList<Message>> messageQueueMap = new HashMap<String, LinkedList<Message>>();
	private Map<String, Continuation> messageContinuationMap = new HashMap<String, Continuation>();
	private ArrayList<Message> messageHistory = new ArrayList<Message>();

	private MessageDAO messageDAO;

	private ScheduledExecutorService messageStoreService;
	private Map<String, Long> userOnlineMap;
	private LinkParser linkParser;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String connectionString = getServletContext().getInitParameter(
				"ConnectionString");
		String username = getServletContext().getInitParameter("Username");
		String password = getServletContext().getInitParameter("Password");
		messageDAO = new MessageDAO(connectionString, username, password);

		getServletContext().setAttribute("MessageHistory", messageHistory);
		// executor check user alive
		messageStoreService = Executors.newScheduledThreadPool(1);
		messageStoreService.scheduleWithFixedDelay(new MessageStore(
				messageHistory, messageDAO),
				ValueHelper.HISTORY_STORE_INIT_DELAY,
				ValueHelper.HISTORY_STORE_DELAY, TimeUnit.MILLISECONDS);
		// link parser
		linkParser = new LinkParser();
	}

	@Override
	public void destroy() {

		super.destroy();

		messageStoreService.shutdown();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	private void pollMessage(HttpServletRequest request,
			HttpServletResponse response, String name) throws IOException {
		// message
		LinkedList<Message> listMessage = messageQueueMap.get(name);
		if (listMessage == null) {

			listMessage = new LinkedList<Message>();
			messageQueueMap.put(name, listMessage);
		}

		// event

		if (listMessage.size() == 0) {
			Continuation continuation = ContinuationSupport
					.getContinuation(request);

			if (continuation.isInitial()) {
				continuation.setTimeout(ValueHelper.GET_LIST_MESSAGE_TIMEOUT);
				continuation.suspend(response);
				messageContinuationMap.put(name, continuation);

			} else {
				PrintWriter out;
				out = response.getWriter();
				out.write("");
				out.flush();
			}

		} else {
			List<Message> sendList = new ArrayList<Message>();
			synchronized (listMessage) {
				while (listMessage.isEmpty() == false) {
					sendList.add(listMessage.poll());
				}

			}

			String jsonObject = gson.toJson(sendList);
			response.setContentType("application/json; charset=UTF-8");
			PrintWriter out;

			out = response.getWriter();
			out.write(jsonObject);
			out.flush();

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			IllegalStateException {

		HttpSession session = request.getSession(true);
		if (session.getAttribute("account") == null) {
			response.setStatus(400);
			response.getWriter().write("No session");
			return;
		}

		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("getListMessage")) {
				MyLogger.proLog.doStartLog("GetListMessage");
				String name = request.getParameter("name");
				pollMessage(request, response, name);
				MyLogger.proLog.doEndLog("GetListMessage");

			}

			else if (action.equals("sendMessage")) {

				MyLogger.log.info("hit message");
				MyLogger.proLog.doStartLog("SendMessage");
				request.setCharacterEncoding("UTF-8");
				Message message = gson.fromJson(request.getReader(),
						Message.class);

				message.setTime(new Timestamp(new Date().getTime()));// set time

				String idFrom = message.getIdFrom();
				String idTo = message.getIdTo();

				if (message.getType() == 1) { // link
					LinkDescriptor ld = linkParser.getInformation(message
							.getContent());
					MyLogger.log.info("title :" + ld.getTitle());
					message.setContent(gson.toJson(ld));
				}

				LinkedList<Message> listMessage;
				Continuation con;
				// userFrom
				listMessage = messageQueueMap.get(idFrom);
				if (listMessage != null) {
					synchronized (listMessage) {
						listMessage.add(message);
					}

					con = messageContinuationMap.get(idFrom);
					synchronized (con) {
						if (con != null && con.isSuspended()) {
							try {
								con.resume();
							} catch (Exception e) {
								// System.out.println(e);
								MyLogger.log.debug(e.toString());
							}
						}
					}
				}

				// userTo
				userOnlineMap = (HashMap<String, Long>) getServletContext()
						.getAttribute("UserOnlineMap");
				if (userOnlineMap != null
						&& !userOnlineMap.keySet().contains(idTo)) { // offline
					message.setOffline(true);
					MyLogger.log.info("send offline to " + idTo);

				} else { // online
					listMessage = messageQueueMap.get(idTo);

					if (listMessage != null) {
						synchronized (listMessage) {
							listMessage.add(message);
						}

						if (messageContinuationMap.get(idTo) != null) {
							con = messageContinuationMap.get(idTo);
							synchronized (con) {
								if (con != null && con.isSuspended()) {
									try {
										con.resume();
									} catch (Exception e) {
										// System.out.println(e);
										MyLogger.log.debug(e.toString());
									}
								}
							}

						}
					}

				}
				synchronized (messageHistory) {
					messageHistory.add(message);// add all msg to history
				}
				MyLogger.proLog.doEndLog("SendMessage");

			}
		}

	}

}

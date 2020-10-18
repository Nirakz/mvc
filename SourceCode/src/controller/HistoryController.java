package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import dao.MessageDAO;

import pojo.Message;
import pojo.Notification;
import util.MyLogger;

public class HistoryController extends HttpServlet {
	private MessageDAO messageDAO;
	private Gson gson;
	private ArrayList<Message> messageHistory;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		gson = new Gson();
		super.init(config);
		String connectionString = getServletContext().getInitParameter(
				"ConnectionString");
		String username = getServletContext().getInitParameter("Username");
		String password = getServletContext().getInitParameter("Password");
		messageDAO = new MessageDAO(connectionString, username, password);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		if (session.getAttribute("account") == null) {
			response.setStatus(400);
			response.getWriter().write("No session");
			return;
		}
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("getNotification")) {
				MyLogger.proLog.doStartLog("GetNotification");
				String id = request.getParameter("id");
				ArrayList<Notification> notifications = messageDAO
						.getNotification(id);
				Map<String, Integer> notificationMap = getNotificationOnMem(id);

				Iterator<Map.Entry<String, Integer>> iter = notificationMap
						.entrySet().iterator();

				while (iter.hasNext()) {
					Map.Entry<String, Integer> entry = iter.next();
					boolean isMerged = false;
					for (Notification notification : notifications) {
						if (notification.getIdFrom().equals(entry.getKey())) {
							notification.setCount(notification.getCount()
									+ entry.getValue());
							isMerged = true;
							break;
						}
					}
					if (isMerged == false) {
						Notification temp = new Notification(entry.getKey(),
								entry.getValue());
						notifications.add(temp);
					}

				}

			
				String jsonObject = gson.toJson(notifications);
				response.setContentType("application/json; charset=UTF-8");
				PrintWriter out;
				out = response.getWriter();
				out.write(jsonObject);
				out.flush();
				MyLogger.proLog.doEndLog("GetNotification");
			} else if (action.equals("getMessageOffline")) {
				MyLogger.proLog.doStartLog("GetMessageOffline");
				String idFrom = request.getParameter("idFrom");
				String idTo = request.getParameter("idTo");
				boolean updateOffline = Boolean.parseBoolean(request.getParameter("updateOffline"));
				ArrayList<Message> listMessageOffline = messageDAO
						.getOfflineMessage(// get from DB
								idFrom, idTo);
				listMessageOffline.addAll(getMessageOfflineOnMem(idFrom, idTo, updateOffline)); // add
																					// message
																					// offline
																					// from
																					// mem
				String jsonObject = gson.toJson(listMessageOffline);
				response.setContentType("application/json; charset=UTF-8");
				PrintWriter out;
				out = response.getWriter();
				out.write(jsonObject);
				out.flush();
				MyLogger.proLog.doEndLog("GetMessageOffline");
			} else if (action.equals("updateOfflineMessage")) {
				MyLogger.proLog.doStartLog("UpdateMessageOffline");
				String jsonArrayID = request.getParameter("arrayID");
				int[] arrayID = gson.fromJson(jsonArrayID, int[].class);
				if (messageDAO.updateOfflineMessage(arrayID)) {
					MyLogger.log.info("update offline success");
				} else {
					MyLogger.log.error("update offline fail");
				}
				MyLogger.proLog.doEndLog("UpdateMessageOffline");
			} else if (action.equals("getHistory")) {
				MyLogger.proLog.doStartLog("GetHistory");
				String idFrom = request.getParameter("idFrom");
				String idTo = request.getParameter("idTo");
				int index = Integer.parseInt(request.getParameter("index"));
				ArrayList<Message> listHistory;						 
				if (index == 0){
					 listHistory = getHistoryOnMem(idFrom, idTo); // get history on memory first
					
				} else {
					listHistory = new ArrayList<Message>();
				}
				listHistory.addAll(messageDAO.getHistory(idFrom, // add history from DB
						idTo, index));
				String jsonObject = gson.toJson(listHistory);
				response.setContentType("application/json; charset=UTF-8");
				PrintWriter out;
				out = response.getWriter();
				out.write(jsonObject);
				out.flush();
				MyLogger.proLog.doEndLog("GetHistory");
			}
		}
	}

	private ArrayList<Message> getHistoryOnMem(String idFrom, String idTo) {
		ArrayList<Message> list = new ArrayList<Message>();
		messageHistory = (ArrayList<Message>) getServletContext().getAttribute(
				"MessageHistory");
		if (messageHistory != null) {
			synchronized (messageHistory) {
				if (messageHistory.size() > 0) {
					Message message;
					for (int i = messageHistory.size() - 1; i >= 0; i--) {
						message = messageHistory.get(i);
						if (message.isOffline() == false) {
							if ((message.getIdFrom().equals(idFrom) && message
									.getIdTo().equals(idTo))
									|| (message.getIdFrom().equals(idTo) && message
											.getIdTo().equals(idFrom))) {
								list.add(message);
							}
						}
					}
				}
			}
		}
		return list;
	}

	private ArrayList<Message> getMessageOfflineOnMem(String idFrom, String idTo, boolean updateOffline) {
		ArrayList<Message> list = new ArrayList<Message>();
		messageHistory = (ArrayList<Message>) getServletContext().getAttribute(
				"MessageHistory");
		if (messageHistory != null) {
			synchronized (messageHistory) {
				if (messageHistory.size() > 0) {
					Message message;
					for (int i = 0; i < messageHistory.size(); i++) {
						message = messageHistory.get(i);
						if (message.isOffline() == true) {
							if (message.getIdFrom().equals(idFrom)
									&& message.getIdTo().equals(idTo)) {
								list.add(message);
								if(updateOffline){
									message.setOffline(false);
								}
							}
						}
					}
				}
			}
		}
		return list;
	}

	private Map<String, Integer> getNotificationOnMem(String id) {
		// ArrayList<Notification> list = new ArrayList<Notification>();
		messageHistory = (ArrayList<Message>) getServletContext().getAttribute(
				"MessageHistory");
		Map<String, Integer> notificationMap = new HashMap<String, Integer>();
		if (messageHistory != null) {
			synchronized (messageHistory) {
				if (messageHistory.size() > 0) {
					Message message;
					for (int i = 0; i < messageHistory.size(); i++) {
						message = messageHistory.get(i);
						if (message.isOffline() == true
								&& message.getIdTo().equals(id)) {
							String idFrom = message.getIdFrom();
							Integer count = notificationMap.get(idFrom);
							if (count != null) {
								int val = count.intValue();
								val ++;
								notificationMap.put(idFrom,val);
							} else {
								notificationMap.put(idFrom, 1);
							}
						}
					}
					
				}
			}
		}
		

		return notificationMap;
	}
}

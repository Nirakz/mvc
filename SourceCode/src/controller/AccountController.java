package controller;



import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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


import com.google.gson.Gson;


import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;

import dao.AccountDAO;
import executor.AliveChecker;
import pojo.Account;
import pojo.UserOnlineData;
import util.HashCode;
import util.MyLogger;
import util.ValueHelper;

/**
 * Servlet implementation class Hello
 */

public class AccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static ArrayList<Account> listAccount;

	private Map<String, Long> userMap = new HashMap<String, Long>();
	private Map<String, Continuation> userContinuationMap = new HashMap<String, Continuation>();
	private AccountDAO accountDAO;
	private Gson gson;

	private String idChangedUser ="";	
	private ScheduledExecutorService scheduledExecutorService;
	private AliveChecker aliveChecker;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		gson = new Gson();

		
		listAccount = new ArrayList<Account>();

		String connectionString = getServletContext().getInitParameter(
				"ConnectionString");
		String username = getServletContext().getInitParameter("Username");
		String password = getServletContext().getInitParameter("Password");
		accountDAO = new AccountDAO(connectionString, username, password);
		listAccount = accountDAO.getListAccount();
		
		//share resource
		getServletContext().setAttribute("UserOnlineMap", userMap);
		
		// executor check user alive
		aliveChecker = new AliveChecker(userMap, userContinuationMap, idChangedUser);
		scheduledExecutorService = Executors.newScheduledThreadPool(5);
		scheduledExecutorService.scheduleWithFixedDelay(
				aliveChecker, ValueHelper.ALIVE_CHECKER_INIT_DELAY, 
				ValueHelper.ALIVE_CHECKER_DELAY, TimeUnit.MILLISECONDS);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		quit(request, response);
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	private void poll(HttpServletRequest request, HttpServletResponse response,
			int countUserOnline, String name) throws IOException {

		if (countUserOnline == userMap.keySet().size()) {

			Continuation continuation = ContinuationSupport
					.getContinuation(request);

			if (continuation.isInitial()) {
				continuation.setTimeout(ValueHelper.GET_USER_ONLINE_TIMEOUT);
				continuation.suspend();
				userContinuationMap.put(name, continuation);
			} else {
				PrintWriter out;
				out = response.getWriter();
				out.write("");
				
			}

		} else {
			//String listOnline = gson.toJson(userOnlineMap.keySet());
			UserOnlineData uoc = new UserOnlineData(userMap.keySet(), idChangedUser);
			response.setContentType("application/json; charset=UTF-8");
			String jsonData = gson.toJson(uoc);
			PrintWriter out;
			out = response.getWriter();
			out.write(jsonData);
		}
	}
	private void quit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		
	
		HttpSession session = request.getSession(true);
		if (session.getAttribute("account") != null) {
			session.removeAttribute("account");
			session.removeAttribute("listUser");
			session.invalidate();
			String name = request.getParameter("username");
			synchronized (userMap) {
				MyLogger.log.info(name + " quit!!!");
				userMap.remove(name);
				
			}
			
			synchronized (idChangedUser) {
				idChangedUser = name;
			}
			
			synchronized (userContinuationMap) {
				userContinuationMap.remove(name);
				for (Continuation con : userContinuationMap.values()) {
					if (con != null && con.isSuspended()) {
						con.resume();
					}
				}
			}
			//storeMessage();
			String action = request.getParameter("action");
			if (action != null) {
				if (action.equals("logout")) {
					
					response.sendRedirect("");
				}
			}
		} else {
			response.setStatus(400);
			response.sendRedirect("login.jsp");
			//response.getWriter().write("No session");
		}

	}


	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("login")) {
				MyLogger.log.info("hit login");
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				for (Account acc : listAccount) {
					if (acc.getUsername().equals(username)) {					
						 //get sha1 for password						

						password=HashCode.sha1(password);

				        
						//try password matching
						if (acc.getPassword().equals(password)) {
							String userID = acc.getId();
							
							//consider whether the acccount logged
							if(userMap.containsKey(userID)){
								HttpSession session = request.getSession(true);
								session.setAttribute("notice",
										"Tài khoản đã được sử dung");
								response.sendRedirect("");
								return;
								
							}
							//storeMessage();
							synchronized (userMap) {
								userMap.put(userID, new Date().getTime()); // update
																					// last
																					// time
							}
							
							synchronized (idChangedUser) {
								idChangedUser = userID;
							}
							synchronized (userContinuationMap) {
								for (Continuation con : userContinuationMap.values()) {
									if(con != null && con.isSuspended()) {
										con.resume();
									}		
								}
							}

							ArrayList<Account> listFriend = (ArrayList<Account>) listAccount
									.clone();
							listFriend.remove(acc);
						

							HttpSession session = request.getSession(true);	
							session.setAttribute("account", acc);
							session.setAttribute("listUser", listFriend);
							
							response.sendRedirect("index.jsp");
							return;
						} else {
							HttpSession session = request.getSession(true);
							session.setAttribute("notice",
									"Tên đăng nhập hoặc mật khẩu không phù hợp");
							response.sendRedirect("");
							return;
						}
					} 
				}
				// username not found
				HttpSession session = request.getSession(true);
				session.setAttribute("notice",
						"Tên đăng nhập hoặc mật khẩu không phù hợp");
				response.sendRedirect("");
				return;
			}
			

			else if (action.equals("getListUser")) {
				
				MyLogger.proLog.doStartLog("GetListUser");
				HttpSession session = request.getSession(true);
				Account account = (Account)session.getAttribute("account");
				if (account != null) {
					synchronized (userMap) {
						userMap.put(account.getId(), new Date().getTime());
					}
					String name = request.getParameter("username");
					int countUserOnline = Integer.parseInt(request
							.getParameter("countUserOnline"));
					poll(request, response, countUserOnline, name);
					MyLogger.proLog.doEndLog("GetListUser");
				} else {
					response.setStatus(400);
					response.getWriter().write("No session");
				}
				
				

				
			}
			else if (action.equals("quit")) {			
				MyLogger.proLog.doStartLog("Quit");
				quit(request, response);
				
				MyLogger.proLog.doEndLog("Quit");
				
				
			}
			
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
		scheduledExecutorService.shutdown();
	}

}

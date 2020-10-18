package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pojo.Account;
import util.HashCode;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import dao.AccountDAO;

public class CRUDController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AccountDAO accountDAO;

	@Override
	public void init() throws ServletException {

		super.init();

		String connectionString = getServletContext().getInitParameter(
				"ConnectionString");
		String username = getServletContext().getInitParameter("Username");
		String password = getServletContext().getInitParameter("Password");

		accountDAO = new AccountDAO(connectionString, username, password);

	}

	private boolean validateID(String id) {
		return id.matches("^(\\d{5})$");
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("action") != null) {
			String action = (String) request.getParameter("action");
			if (action.equals("list")) {
				try {

					// paging arguments
					String jtStartIndex = request.getParameter("jtStartIndex");
					System.out.println("start index : " + jtStartIndex);

					String jtPageSize = request.getParameter("jtPageSize");
					System.out.println("page size : " + jtPageSize);

					List<Account> lstAccount = new ArrayList<Account>();

					// Fetch Data from account table in database

					lstAccount = accountDAO.getListAccountForAdmin();
					int count = lstAccount.size();

					// paging
					int start_index = Integer.parseInt(jtStartIndex);
					int to_index = start_index + Integer.parseInt(jtPageSize);
					if (to_index <= lstAccount.size()) {
						lstAccount = lstAccount.subList(start_index, to_index);
					} else {
						lstAccount = lstAccount.subList(start_index,
								lstAccount.size());
					}

					// Convert Java Object to Json
					Gson gson = new Gson();
					JsonElement element = gson.toJsonTree(lstAccount,
							new TypeToken<List<Account>>() {
							}.getType());
					JsonArray jsonArray = element.getAsJsonArray();
					String listData = jsonArray.toString();

					// Return Json in the format required by jTable plugin
					listData = "{\"Result\":\"OK\",\"Records\":" + listData
							+ ",\"TotalRecordCount\":" + count + "}";
					response.setContentType("application/json");
					response.setHeader("Cache-control", "no-cache, no-store");
					response.setHeader("Pragma", "no-cache");
					response.setHeader("Expires", "-1");

					response.getWriter().print(listData);

					System.out.println(listData);

				} catch (Exception ex) {
					String error = "{\"Result\":\"ERROR\",\"Message\":\""
							+ ex.getStackTrace() + "\"}";
					response.getWriter().print(error);
					System.out.println(error);
				}

			}// end action=list

			else if (action.equals("create") || action.equals("update")) {
				try {
					String id = request.getParameter("id");
					String username = request.getParameter("username");

					String avatar = request.getParameter("avatar");
					String fullName = request.getParameter("fullName");

					// validating id
					if (!validateID(id)) {
						// return error code
						String error = "{\"Result\":\"ERROR\",\"Message\":\""
								+ id + " is not legal\"}";
						response.getWriter().print(error);
						System.out.println(error);
						return;
					}

					Account anAccount = new Account();

					anAccount.setId(id);
					anAccount.setUsername(username);

					anAccount.setAvatar(avatar);
					anAccount.setFullName(fullName);

					System.out.println(id);
					System.out.println(username);

					if (action.equals("create")) {
						String password = request.getParameter("password");
						String confirmPassword = request
								.getParameter("confirmPassword");

						// validate password and confirmPassword
						if (!password.equals(confirmPassword)) {
							// return error code
							String error = "{\"Result\":\"ERROR\",\"Message\":\" two passwords are not same\"}";
							response.getWriter().print(error);
							System.out.println(error);
							return;
						}

						// update in AccountController
						if (AccountController.listAccount != null) {
							AccountController.listAccount.add(anAccount);
						}// end if
							// password information for creating
							// take sha1 for password
						password = HashCode.sha1(password);
						anAccount.setPassword(password);
						// insert to database here
						accountDAO.addAccount(anAccount);
					} else {
						// update in AccountController
						if (AccountController.listAccount != null) {						
								for (Account acc : AccountController.listAccount) {
									if (acc.getId().equals(id)) {
										int indx = AccountController.listAccount
												.indexOf(acc);
										//keep password no change.
										anAccount.setPassword(acc.getPassword());
										AccountController.listAccount.set(indx,
												anAccount);
										break;
									}
								}							
						}// end if
							// password not allow editing--no change-no
							// transmitted for upadating
							// update record in database
						accountDAO.updateAccount(anAccount);
					}

					Gson gson = new Gson();

					String jsonString = gson.toJson(anAccount);

					// Return Json in the format required by jTable plugin
					jsonString = "{\"Result\":\"OK\",\"Record\":" + jsonString
							+ "}";

					response.setContentType("application/json");
					response.setHeader("Cache-control", "no-cache, no-store");
					response.setHeader("Pragma", "no-cache");
					response.setHeader("Expires", "-1");

					response.getWriter().print(jsonString);

					System.out.println(jsonString);

				} catch (Exception ex) {

					String error = "{\"Result\":\"ERROR\",\"Message\":\""
							+ ex.getStackTrace() + "\"}";
					response.getWriter().print(error);
					System.out.println(error);
				}
			}// end action=create or action = update

			else if (action.equals("delete")) {
				try {
					String id = request.getParameter("id");

					// validating id
					if (!validateID(id)) {
						// return erro code
						String error = "{\"Result\":\"ERROR\",\"Message\":\""
								+ id + " is not legal\"}";
						response.getWriter().print(error);
						System.out.println(error);
						return;
					}
					
					if (AccountController.listAccount != null) {
						int indx = -1;
						for (Account acc : AccountController.listAccount) {
							if (acc.getId().equals(id)) {
								 indx = AccountController.listAccount
										.indexOf(acc);
								 break;
							}	
						}
						if( indx !=-1 ){
							AccountController.listAccount.remove(indx);
						}
				    }// end if
					
					accountDAO.deleteAccount(id);

					String responseString = "{\"Result\":\"OK\"}";

					response.getWriter().print(responseString);
				} catch (Exception ex) {
					String error = "{\"Result\":\"ERROR\",\"Message\":\""
							+ ex.getStackTrace() + "\"}";
					response.getWriter().print(error);
					System.out.println(error);
				}
			} // end action=delete
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

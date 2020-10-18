package util;

import org.eclipse.jetty.server.Server;

import org.eclipse.jetty.webapp.WebAppContext;


public class WebChatServer {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
	/*	Server server = new Server(8080);
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
		resourceHandler.setWelcomeFiles(new String[]{"index.html"});
		resourceHandler.setResourceBase("./WebContent/");
		
		ServletContextHandler contextHandler = new ServletContextHandler();
		contextHandler.setContextPath("/demo3");
	
		contextHandler.addServlet(AccountController.class, "/api/user/*");
		contextHandler.addServlet(MessageController.class, "/api/message/*");
		
		HandlerList handlerList = new HandlerList();
		handlerList.setHandlers(new Handler[]{resourceHandler,contextHandler});
		
		server.setHandler(handlerList);
		server.start();
		server.join();*/
		String webappDirLocation = "./WebContent/";

		Server server = new Server(8080);

		WebAppContext root = new WebAppContext();
		
		root.setDescriptor(webappDirLocation + "WEB-INF/web.xml");
		root.setResourceBase(webappDirLocation);
		root.setContextPath("/WebChat");
		server.setHandler(root);
		server.start();
		server.join();
		
	}

}

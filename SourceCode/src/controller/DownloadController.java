package controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HashCode;
import util.ValueHelper;

public class DownloadController extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {
	static final long serialVersionUID = 1L;
	private static final int BUFSIZE = 4096;
	public static final String FileDir = "uploadFiles/";
	//for logging
	private Logger log;
	public void init() {
		log = LoggerFactory.getLogger(this.getClass());
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);
		if (session.getAttribute("account") == null) {
			response.setStatus(401);
			response.getWriter().write("<b>401 Unauthorized<b>");
			return;
		}


		String fileDir=request.getSession().getServletContext().getRealPath("/")
				+ File.separator + FileDir;
        String filePath="";
		String downloadedFileName = request.getParameter("fileName");
		
		if (downloadedFileName != null) {
			filePath =fileDir+ downloadedFileName;
		}

		final File file = new File(filePath);
		int length = 0;
		ServletOutputStream outStream = response.getOutputStream();
		ServletContext context = getServletConfig().getServletContext();
		String mimetype = context.getMimeType(filePath);

		// sets response content type
		if (mimetype == null) {
			mimetype = "application/octet-stream";
		}
		response.setContentType(mimetype);
		response.setContentLength((int) file.length());
		String fileName = (new File(filePath)).getName();
        
		//get display name 
		String displayName = HashCode.removeMD5prefix(fileName).substring(12);
		
		log.info("display name for downloading :" + displayName +"\n");
		
		
		
		// sets HTTP header
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ displayName + "\"");

		byte[] byteBuffer = new byte[BUFSIZE];
		DataInputStream in = new DataInputStream(new FileInputStream(file));

		// reads the file's bytes and writes them to the response stream
		while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
			outStream.write(byteBuffer, 0, length);
		}

		in.close();
		outStream.close();

		// delete file after downloading 5 minutes

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				log.info("Deadline to delete file "+file.getName() + "\n");
				if (file.delete()) {
					log.info(file.getName() + " : is deleted\n");
				} else {
					log.info("\n"+file.getName() +" : delete operation is failed\n");
				}
			}
		}, ValueHelper.DELETE_FILE_INTERVAL);// 5 minutes
	}
}

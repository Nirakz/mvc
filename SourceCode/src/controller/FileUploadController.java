package controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HashCode;

import com.google.gson.JsonObject;

public class FileUploadController extends HttpServlet {

	private static final long serialVersionUID = 3447685998419256747L;
	private static final String RESP_SUCCESS = "{\"jsonrpc\" : \"2.0\", \"result\" : \"success\", \"id\" : \"id\"}";
	private static final String RESP_ERROR = "{\"jsonrpc\" : \"2.0\", \"error\" : {\"code\": 101, \"message\": \"Failed to open input stream.\"}, \"id\" : \"id\"}";
	public static final String JSON = "application/json";
	public static final int BUF_SIZE = 2 * 1024;
	public static final String FileDir = "uploadFiles/";
	//for logging
	private Logger log;
	@Override
	public void init() throws ServletException {
		super.init();
		log = LoggerFactory.getLogger(this.getClass());
	}
	
	/**
	 * Handles an HTTP POST request from Plupload.
	 * 
	 * @param req
	 *            The HTTP request
	 * @param resp
	 *            The HTTP response
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String responseString = RESP_SUCCESS;

		int chunk = 0;
		int chunks = 0;
		String fileName = "";
		String idFrom = "";
		String idTo = "";

		String fileDir = req.getSession().getServletContext().getRealPath("/")
				+ "/" + FileDir;
		
		File dstFileDir = new File(fileDir);

		if (!dstFileDir.exists()) {
			dstFileDir.mkdirs();
		}

		boolean isMultipart = ServletFileUpload.isMultipartContent(req);

		if (isMultipart) {

			ServletFileUpload upload = new ServletFileUpload();

			try {
				
				FileItemIterator iter = upload.getItemIterator(req);

				while (iter.hasNext()) {

					FileItemStream item = iter.next();
					InputStream input = item.openStream();

					// Handle a form field.
					if (item.isFormField()) {

						String fieldName = item.getFieldName();
						String value = Streams.asString(input);

						if ("name".equals(fieldName)) {
							log.info("uploaded fileName is: " + value +"\n");
							// prevent fullpath from IE10
							// value= FilenameUtils.getName(value);
							fileName = value;
						} else if ("chunks".equals(fieldName)) {
							chunks = Integer.parseInt(value);
						} else if ("chunk".equals(fieldName)) {
							chunk = Integer.parseInt(value);
						} else if ("idFrom".equals(fieldName)) {
							idFrom = value;
						} else if ("idTo".equals(fieldName)) {
							idTo = value;
						}

					}

					// Handle a multi-part MIME encoded file.
					else {

						// make fileName specific for idFrom and idTo
						fileName = idFrom + "_" + idTo + "_" + fileName;

						File dstFile = new File(dstFileDir.getPath() + "/"
								+ fileName);
						//have fileName in folder in case of losing network; reupload from beginning
                        if(chunk==0&&dstFile.exists()) {
                        	log.info("old file exists: deleted");
                        	dstFile.delete();
                        }
						// save path for file
						log.info("save path for file with temp new name :" + dstFile.getPath() + "\n");

						saveUploadFile(input, dstFile);
					}
				}
			} catch (Exception e) {
				responseString = RESP_ERROR;
				e.printStackTrace();
			}
		}

		// Not a multi-part MIME request.
		else {
			responseString = RESP_ERROR;
		}

		// if file is not divided
		// or completely uploaded
		if (chunks == 0 || chunk == chunks - 1) {
			log.info("Uploaded file: " + fileName + "\n");
			log.info("send file - idFrom: " + idFrom + "\n");
			log.info("send file - idTo: " + idTo + "\n");

			// make unique name for filenam
			File dstFile = new File(dstFileDir.getPath() + "/" + fileName);
			String tempName = dstFile.getName();

			tempName = HashCode.makeUniqueName(tempName);

			File dstNewFile = new File(dstFileDir.getPath() + "/" + tempName);
			dstFile.renameTo(dstNewFile);

			log.info(" final new name for uploaded file: " + dstNewFile.getName()+"\n");

			// final send chat
			 HttpSession session = req.getSession(true);
			sendPostRequest(session.getId(),idFrom, idTo, dstNewFile.getName());
			
			log.info("complete for sending text for fileName.\n");
		}

		resp.setContentType(JSON);

		byte[] responseBytes = responseString.getBytes();

		resp.setContentLength(responseBytes.length);

		ServletOutputStream output = resp.getOutputStream();

		output.write(responseBytes);
		output.flush();
	}

	/**
	 * Saves the given file item (using the given input stream) to the web server's
	 * local temp directory.
	 * 
	 * @param input The input stream to read the file from
	 * @param dst The dir of upload
	 */
	public void saveUploadFile(InputStream input, File dst) throws IOException {
		
		OutputStream out = null;
		
		try {
			if (dst.exists()) {
				out = new BufferedOutputStream(new FileOutputStream(dst, true),
						BUF_SIZE);
			} else {
				out = new BufferedOutputStream(new FileOutputStream(dst),
						BUF_SIZE);
			}

			byte[] buffer = new byte[BUF_SIZE];
			int len = 0;
			
			while ((len = input.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// send request to other servlet
	public  void sendPostRequest(String sessionId, String idFrom, String idTo, String fileName) {

		// Build parameter string
		// String data =
		// "{idFrom:\"00001\",idTo:\"00002\",content:\"thu nghiem nha\"}";

		JsonObject json = new JsonObject();
		String displayName=HashCode.removeMD5prefix(fileName).substring(12);
		
		json.addProperty("idFrom", idFrom);
		json.addProperty("idTo", idTo);
		json.addProperty("type",3);//file type
		json.addProperty(
				"content",
				"<div><b>attached file:</b> <a href=\"http://localhost:8080/WebChat/downloadServlet?fileName="
						+ fileName + "\">" + displayName + "</a></div><br/>");
		
		try {

			// Send the request
			URL url = new URL("http://localhost:8080/WebChat/api/message?action=sendMessage");
			
			URLConnection conn = url.openConnection();
			conn.setRequestProperty(
        		    "Cookie","JSESSIONID=" + sessionId);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

			// write parameters
			writer.write(json.toString());
			writer.flush();

			// Get the response
			StringBuffer answer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null) {
				answer.append(line);
			}
			
			writer.close();
			reader.close();

			// Output the response
			System.out.println(answer.toString());

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}// end sendRequest method
}

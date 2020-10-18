package controller;
 
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HashCode;
import util.MyLogger;

import com.google.gson.JsonObject;


public class ImageUploadController extends HttpServlet {
    //for logging
	private Logger log;
	
	@Override
	public void init() throws ServletException {
		super.init();
		log = LoggerFactory.getLogger(this.getClass());		
	}
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	
    	MyLogger.proLog.doStartLog("doSendImage");
    	
    	response.setContentType("text/html;charset=UTF-8");
    	
        PrintWriter out = response.getWriter();
        
        try {
	    	// get access to file that is uploaded from client
	        Part p1 = request.getPart("imageName");
	        InputStream is = p1.getInputStream();
	        String fileName = extractFileName(p1);
	        
	        log.info("extracted image's name is "+fileName + "\n");
	        
	        //prevent fullpath from ie 10
	        fileName = FilenameUtils.getName(fileName);
	        log.info("extracted from fullpath (if in IE) "+fileName + "\n");
	        
	        // read  idFRom which is sent as a part
	        Part p2  = request.getPart("idFrom");
	        Scanner s = new Scanner(p2.getInputStream());	        
	        String idFrom = s.nextLine(); 
	        
	        //read idTo with is sent as a part
	        Part p3  = request.getPart("idTo");
	        s = new Scanner(p3.getInputStream());	        
	        String idTo = s.nextLine();   
	 
	        //make unique name for imageName;
	        fileName = HashCode.makeUniqueName(fileName);
	        
	        // get path on the server
	        String outputfile = getServletContext().getRealPath("/uploadFiles/images/"+fileName);  
	        
	        //check whether the image exists
	        File imageFile = new File(outputfile);
	        
	        //if it not exists
	        if(!imageFile.exists())
	        {
		        FileOutputStream os = new FileOutputStream (outputfile);
		       
		        // write bytes taken from uploaded file to target file
		        int ch = is.read();
		        while (ch != -1) {
		             os.write(ch);
		             ch = is.read();
		        }
		        os.close();
	        }
	        
	        out.println("<h3>File uploaded successfully!</h3>");
	        
	        //read size of image
	        BufferedImage sourceImage = ImageIO.read(imageFile);
	        int width = sourceImage.getWidth();
	        int height = sourceImage.getHeight();
	        
	        log.info("original width: "+width + "\n");
	        log.info("original height: "+height + "\n");
	        
	        //scale height weight if necessary	        
//	        if (width>180){
//	        	height = (int)Math.round((height*1.0/width)*180);
//	        	width =180;
//	        }
//	        log.info("new width: "+width + "\n");
//	        log.info("new height: "+height + "\n");
	        
	        //close stream  from network
	        is.close();	       
	        
	        //final  send chat 
	        log.info("idFrom - send image : "+idFrom+"\n");
	        log.info("idTo - send image : "+idTo + "\n");
	        
	        
	        HttpSession session = request.getSession(true);
	        
	        sendPostRequest(session.getId(),idFrom, idTo, fileName,width,height);
	        
	        log.info("complete sending Image \n");
	        
	        MyLogger.proLog.doEndLog("doSendImage");
	        
        }
	    catch(Exception ex) {
	    		out.println("Exception -->" + ex.getMessage());
	    }
	    finally { 
	    		out.close();
	    }
       
    }
    /**
     * Extracts file name from HTTP header content-disposition
     */
    public String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }
    
  //send request to other servlet
  	public  void sendPostRequest(String sessionId, String idFrom, String idTo,String imageName, int width, int height) {

          //Build parameter string
          //String data = "{idFrom:\"00001\",idTo:\"00002\",content:\"thu tinh nha\"}";
          
          JsonObject json = new JsonObject();
          json.addProperty("idFrom", idFrom);
          json.addProperty("idTo", idTo);
          json.addProperty("type", 2); // image type 
          json.addProperty(
  				"content",
  				"<div style=\"max-width:"+ width +"px !important; max-height:"+height+"px !important\"><a class=\"fancybox\" rel=\"group\" href=\"uploadFiles/Images/"
  						+ imageName
  						+ "\"><img id=\"img_up_"
  						+ idTo
  						+ "\" class=\"send_image\" src=\"uploadFiles/Images/"
  						+ imageName + "\" ></a></div>");
         
          try {

              // Send the request
              URL url = new URL("http://localhost:8080/WebChat/api/message?action=sendMessage&authen=server");
              URLConnection conn = url.openConnection();
              conn.setRequestProperty(
            		    "Cookie","JSESSIONID=" + sessionId);
              conn.setRequestProperty("Content-Type", "application/json");
              conn.setRequestProperty("Accept", "application/json");
              conn.setDoOutput(true);
              
              OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

              //write parameters
              writer.write(json.toString());
              writer.flush();

              // Get the response
              StringBuffer answer = new StringBuffer();
              BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
              String line;
              
              while ((line = reader.readLine()) != null) {
                  answer.append(line);
              }
              
              writer.close();
              reader.close();

              //Output the response
              System.out.println(answer.toString());

          } catch (MalformedURLException ex) {
              ex.printStackTrace();
          } catch (IOException ex) {
              ex.printStackTrace();
          }
  	}//end sendRequest method
}
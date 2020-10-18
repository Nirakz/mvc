<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <%@ page import="pojo.Account" %>
<%
   Account account = (Account)session.getAttribute("account");
   if(account==null)
   {
	   response.setStatus(401);
	   response.getWriter().write("<b>401 Unauthorized<b>");
	   return;
   }
%>
<html>
<head>
<title>Upload Files</title>
<link rel="stylesheet" type="text/css" href="plupload/jquery.plupload.queue/css/jquery.plupload.queue.css" type="text/css" media="screen" />
<script type="text/javascript" src="js/jquery-1.10.2.min.js" ></script>
<script type="text/javascript" src="plupload/plupload.full.js"></script>
<script type="text/javascript" src="plupload/jquery.plupload.queue/jquery.plupload.queue.js"></script>
<script type="text/javascript" src="plupload/i18n/cn.js"></script>
<script type="text/javascript">
/* Convert divs to queue widgets when the DOM is ready */
$(function(){
	function pluploadFunc(idFrom, idTo){
		$("#uploader").pluploadQueue({
			// General settings
			runtimes : 'html5,gears,browserplus,silverlight,flash,html4',
			container : 'uploadcontainer',
			url : 'fileUploadServlet',
			max_file_size : '10mb',
			unique_names : false,
			chunk_size: '2mb',
			// Specify what files to browse for
			filters : [
				{title: "Image files", extensions: "jpg,gif,png"},
				{title: "Zip files", extensions: "zip"}
			],	
			// Flash settings
			flash_swf_url : 'plupload/plupload.flash.swf',
			// Silverlight settings
			silverlight_xap_url : 'plupload/plupload.silverlight.xap',
			multipart_params: {'idFrom':idFrom,'idTo':idTo},
			//config event
			init : {
				Error: function(up, args) {
					                // Called when a error has occured
					                console.log('[error] roi nhe )');
		                            window.alert(args.message+args.file.name);	              
		       },
				UploadProgress: function(up, file) {	
					
			     // Called while a file is being uploaded
					console.log("upload progress here");
					var uploader = $('#uploader').pluploadQueue();
		        	var dropElm = document.getElementById(uploader.settings.drop_element);
		   	      	plupload.removeEvent(dropElm, 'dragenter', up.id);
		   	     	plupload.removeEvent(dropElm, 'dragover', up.id);
		   	      	plupload.removeEvent(dropElm, 'drop', up.id);
			     },		        
		        UploadComplete: function(up, files) {
		        	console.log("upload completed"); 
		        	var j
		        	for(j=0;j<up.files.length;j++){
		        		var file = up.files[j];
		        		if(file.status==9999){
		        			up.total.loaded -= file.size;
		        		    up.total.percent=Math.round(up.total.loaded/up.total.size*100);
		        		    $('span.plupload_total_status').html(up.total.percent + '%');
		        		}
		        	}
		         },
		         ChunkUploaded: function(up, file, response){ 
                         if(response.status==0) {
                        	 up.stop();
                        	 file.status = plupload.FAILED;
	                         up.trigger('QueueChanged');            
	                         up.trigger('UploadProgress', file);   
	                         up.start();
                         }
                         var data = jQuery.parseJSON(response.response);
                         if (data.error){
                        	 up.stop();
                             file.status = plupload.FAILED;
                             up.trigger('QueueChanged');            
                             up.trigger('UploadProgress', file);   
                             up.start();
                         }		            	             
		         },
		         FileUploaded: function(up, file, response) {
		          // Called when a file has finished uploading
		        	 if(response.status==0) {
		        		     up.stop();		        		     
		        		     up.total.loaded -= file.size;
		        		     up.total.percent=Math.round(up.total.loaded/up.total.size*100);
		        		     file.percent=0;
		        		     file.status = 9999;
	                         up.trigger('QueueChanged'); 
	                         up.start();
                         }		        	 
		          }//end FileUploaded

		      }//end init
			
		}); //end plUploadQueue
	}//end plUploadFunc
	
	var idFrom = String('<%=request.getParameter("idFrom")%>');
	var idTo = String('<%=request.getParameter("idTo")%>');
	console.log(idFrom);
	console.log(idTo);
	pluploadFunc(idFrom,idTo);
	$('#clear').click(function(){
		pluploadFunc(idFrom,idTo);
	});
});
</script>

</head>

<body>
	<div id="uploadcontainer">
		<div style="width: 750px; margin: 0px auto">
			<form id="formId" action="Submit.action" method="post">
				<div id="uploader">
					<p>You browser doesn't have Flash, Silverlight, Gears, BrowserPlus or HTML5 support.</p>
				</div>
				<input type="button" value="Re-Upload" id="clear"/>
			</form>
		</div>
	</div>
</body>

</html>
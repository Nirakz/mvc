<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <%@ page import="pojo.Account" %>
<%
   Account account = (Account)session.getAttribute("account");
   if(account==null){   
	   response.sendRedirect("");
   }
   if(account!=null && !account.getId().equals("00000")){//not an admin
      response.sendRedirect("");
   }
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin Page</title>
<!-- Include one of jTable styles. -->
<link href="css/metro/crimson/jtable.css" rel="stylesheet" type="text/css" />
<link href="css/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css" />
<!-- Include jTable script file. -->
<script src="js/jquery-1.10.2.min.js" type="text/javascript"></script>
<script src="js/jquery-ui-1.10.3.custom.js" type="text/javascript"></script>
<script src="js/jquery.jtable.js" type="text/javascript"></script>

<script type="text/javascript">
    var mymessages = {
    	 addNewRecord: 'Add a user',
       	 editRecord: 'Edit this user'	
    };
    $.extend(true, $.hik.jtable.prototype.options, {
        messages: mymessages,
    	deleteConfirmation: function(data) {
    	    data.deleteConfirmMessage = 'Are you sure to delete account ' + data.record.id + '?';
    	}
    });
</script>


<script type="text/javascript">
    $(document).ready(function () {
        //initialize jTable
        $('#AccountTableContainer').jtable({
        	paging: true, //Enable paging
            pageSize: 10, //Set page size (default: 10)
            title: 'Table of Users',
            actions: {
                listAction: 'CRUDController?action=list',
                createAction:'CRUDController?action=create',
                updateAction: 'CRUDController?action=update',
                deleteAction: 'CRUDController?action=delete'
            },
            
            updateRecord: (function() { alert("my custom action.");}),
            
            fields: {
                id: {
                   
                	key: true,
                	create: true,
                    edit: true,
                    list: true,
                    title: 'Id_Account',
                    width: '30%',
                    input: function (data) {
                        if (data.record) {
                            return '<input type="text" name="id"  value="' + data.record.id + '" readonly />';
                        } else {
                            return '<input type="text" name="id"  value="5-digit ID" />';
                        }
                    }
                },
                username: {
                    title: 'User Name',
                    width: '30%'
                },
                password: {
                    title: 'Password',
                    list: false,
                    edit:false,
                    type:'password'
                },
                confirmPassword: {
                	title:'Confirm Password',
                	list:false,
                	edit:false,
                	type:'password'
                },
                avatar: {
                    title: 'Avatar',
                    width: '20%',
                },
                fullName: {
                    title: 'Full Name',
                    width: '20%',
                    
                }
                
            },
            //when create form for creating or editing
            formCreated :function(event,data){
            	if(data.formType=='create'){
            		//clear id field when it is focused
            		data.form.find('input[name="id"]').keypress(function() {
            			
            			console.log(" focused id");
            			var id= data.form.find('input[name="id"]').val();
            			if(id=="5-digit ID"){
            				data.form.find('input[name="id"]').val("");
            			}
            		});
            		
            		//onblur
					data.form.find('input[name="id"]').blur(function() {
            			
            			console.log(" blur id");
            			var id= data.form.find('input[name="id"]').val();
            			if(id==""){
            				data.form.find('input[name="id"]').val("5-digit ID");
            			}
            		});
            	}
            	
            },
            //validate when submit
            formSubmitting: function(event, data) {
            	
            	var validating=true;
            	var id= data.form.find('input[name="id"]').val();
            	var username = data.form.find('input[name="username"]').val();
            	var fullName = data.form.find('input[name="fullName"]').val();
            	//REGEX for 6-digit number format
            	var RE = /^\d{5}$/;
            	
            	console.log(id);
            	
            	validating = RE.test(id);
            	if(!validating){
            		console.log("id la "+id);
            		console.log("test 5 digit number fails");
            		window.alert(" please enter 5-digit number in Id Account !");
            		data.form.find('input[name="id"]').val("5-digit ID");
            		return validating;
            	}
            	RE=/^(\s)*$/;
            	validating=!RE.test(username)
            	if(!validating){
            		window.alert("UserName is not blank!");
            		return validating;
            	}
        	    validating = !RE.test(fullName);
        		if(!validating){
        			alert("Full Name is not blank!");
            		return validating;
        		}
        		
        		var password = data.form.find('input[name="password"]').val();
        		var confirmPassword= data.form.find('input[name="confirmPassword"]').val();
        		
        		if( password != confirmPassword) {
        			window.alert("two passwords are not same !");
        			validating = false;
        			return validating;
        		}
            	return validating;
    		}//end validatding
        });  
        $('#AccountTableContainer').jtable('load');
        
       

    });
</script>
</head>
<body>
<h4><a href="index.jsp">return to chat page.</a></h4>
<div style="width:60%;margin-right:20%;margin-left:20%;text-align:center;">
<h1>List of Users</h1>
<div id="AccountTableContainer"></div>
</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en"> <!--<![endif]-->
 <%@ page import="pojo.Account" %>
<%
	
		Account account = (Account)session.getAttribute("account");
		   if(account!=null)
		   {
			   response.sendRedirect("index.jsp");
		   }	
	
   
%>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Login Form</title>
  <link rel="stylesheet" href="css/login.css">
  <script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
  <script type="text/javascript" src="js/login.js"></script>
  <script>
  
  </script>
</head>
<body>
  <div class="container">
    <div class="login">
      <h1>Login to Web App</h1>
      <form method="post" action="account?action=login">
        <p><input type="text" name="username" value="" placeholder="Username"></p>
        <p><input type="password" name="password" value="" placeholder="Password"></p>
        <%String notice =session.getAttribute("notice")!=null?(String)session.getAttribute("notice"):""; %>
        <%session.removeAttribute("notice"); %>
        <div class ='notice'><%=notice%></div>  
        <p class="submit"><input type="submit" name="commit" value="Login"></p>
      </form>
    </div>
  </div>
</body>
<script>

</script>
</html>

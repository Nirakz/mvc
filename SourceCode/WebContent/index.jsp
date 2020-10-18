<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <%@ page import="pojo.Account" %>
<%
   Account account = (Account)session.getAttribute("account");
   if(account==null)
   {
	   response.sendRedirect("");
   }
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Webchat Servlet</title>
<link rel="stylesheet" type="text/css" href="css/jquery.jscrollpane.css" />
<link rel="stylesheet" type="text/css" href="css/jquery-ui-1.10.3.custom.css" />
<link rel="stylesheet" type="text/css" href="css/main.css" />
<link rel="stylesheet" type="text/css" href="css/chatbox.css" />
<link rel="stylesheet" type="text/css" href="css/emo.css" />
<link rel="stylesheet" type="text/css" href="css/message.css" />
<link rel="stylesheet" type="text/css" href="css/jquery.fancybox.css" />

<script type="text/javascript">
	all_chatboxes = new Array(); 	// Array contains all all_chatboxes
	listUser = new Array();			// List online user
</script>
<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.2.1.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.10.3.custom.js"></script>
<script type="text/javascript" src="js/layout.js"></script>
<script type="text/javascript" src="js/jquery.jscrollpane.min.js"></script>
<script type="text/javascript" src="js/jquery.mousewheel.js"></script>
<script type="text/javascript" src="js/mwheelIntent.js"></script>
<script type="text/javascript" src="js/emotion.js"></script>
<script type="text/javascript" src="js/util.js"></script>
<script type="text/javascript" src="js/chat.js"></script>
<script type="text/javascript" src="js/jquery.fancybox.js"></script>
<script type="text/javascript">
	window.onload = initIndex;
</script>
</head>
<body>
	<%-- <div id="toolbar">
		<div id="curr_u_photo">
			<img class="curr_u_photo_img" src="images/user_avatar/${requestScope.account.avatar }.jpg" />
		</div>
		<div id="curr_u_name">
			<c:out value="${requestScope.account.fullName }"></c:out>
		</div>
		<div>
			<a id="logout_url" href="account?action=logout">Đăng xuất</a>
		</div>
	</div> --%>
	<div id="toolbar">
		<div id="curr_u_photo">
			<img class="curr_u_photo_img" src="images/user_avatar/${sessionScope.account.avatar }.jpg" />
		</div>
		<div id="curr_u_name">
			<c:out value="${sessionScope.account.fullName }"></c:out>
		</div>
		<div>
			<a id="logout_url" href="account?action=logout&username=${sessionScope.account.id }">Đăng xuất</a>
		</div>
		<%
		   if(account.getId().equals("00000"))
			   out.println("<div> <a id='link_admin' href='admin.jsp'> Move to admin page</a></div>");
		%>
	</div>
	<input type="hidden" id="myID" value="${sessionScope.account.id}">
	<%-- <input type="hidden" id="myID" value="${requestScope.account.id}"> --%>
	<div id="list_friend" class="list">
		<div id="list_friend_title">
			<div class="list_friend_title_icon"></div>
			<div class="list_friend_title_name">List friend</div>
		</div>
		
		<div class="list_error_connect">
			Không thể kết nối với máy chủ
			<div>Đang thử kết nối lại</div>
			<div class="wrap_reconnect_bt">
				<button class="reconnect_bt"></button>
			</div>
		</div>
		
		<div id="list_friend_content" class="list_friend_content_area">
			
			<c:forEach var="user" items="${sessionScope.listUser}">
			<div id="${user.id }" class="list_friend_item">
				<div class="user_avatar">
					<img src="images/user_avatar/${user.avatar }.jpg" />
				</div>
				<div class="user_name"><c:out value="${user.fullName }"></c:out></div>
				<div class="notification" style="color:red"></div>
			</div>
			</c:forEach>
		<%-- 	<c:forEach var="user" items="<%= request.getAttribute( "listUser" ) %>">
			<div id="${user.id }" class="list_friend_item">
				<div class="user_avatar">
					<img src="images/user_avatar/${user.avatar }.jpg" />
				</div>
				<div class="user_name"><c:out value="${user.fullName }"></c:out></div>
				<div class="notification" style="color:red"></div>
			</div>
			</c:forEach>
 --%>
			
		</div>
		<div id="search_friend">
			<input type="text" id="search_friend_field" name="search_friend_field" placeholder="Tìm kiếm" />
		</div>
		<div class="wrap_notify">
		</div>
	</div>
</body>
</html>
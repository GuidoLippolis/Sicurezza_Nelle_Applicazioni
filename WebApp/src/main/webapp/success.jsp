<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<h1> Login Successfull </h1>

<%

	String user = null;

	if(session.getAttribute("user") == null)
		response.sendRedirect("./index.jsp");
	else
		user = (String) session.getAttribute("user");

%>

<h3> Hi, <%= user %> </h3> <br>

<a href="LogoutServlet"> Logout </a>

</body>
</html>
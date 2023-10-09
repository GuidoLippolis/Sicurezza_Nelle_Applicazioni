<%@page import="utils.Utils"%>
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
    String user = (String) session.getAttribute("user");
    String rememberedUser = null;

    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
    	
        for (Cookie cookie : cookies) {
        	
            if (cookie.getName().equals("rememberMe")) {
            	
                String cookieValue = cookie.getValue();
                
                rememberedUser = Utils.getUsernameFromCookie(cookieValue);
                
                break;
                
            }
        }
    }

    if (user != null) {
        // User is logged in via session
%>
        <h3> Hi, <%= user %> </h3> <br>
        <a href="LogoutServlet"> Logout </a>
<%
    } else if (rememberedUser != null) {
%>
        <h3> Hi, <%= rememberedUser %> </h3> <br>
        <a href="LogoutServlet"> Logout </a>
<%
    } else
    	
        response.sendRedirect("./index.jsp");
    
%>


</body>
</html>
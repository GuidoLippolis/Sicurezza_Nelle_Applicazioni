<%@page import="utils.Utils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Login Successful</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f7f7f7;
            margin: 0;
            padding: 0;
        }

        h1 {
            text-align: center;
            margin-top: 100px;
            font-size: 36px;
            color: #333;
        }

        h3 {
            text-align: center;
            font-size: 24px;
            color: #0074cc;
        }

        a {
            display: block;
            text-align: center;
            text-decoration: none;
            background-color: #0074cc;
            color: #fff;
            padding: 10px 20px;
            margin: 20px auto;
            border-radius: 5px;
            width: 150px;
            transition: background-color 0.3s ease;
        }

        a:hover {
            background-color: #0058a0;
        }
    </style>
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
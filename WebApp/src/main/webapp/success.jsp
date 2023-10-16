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
            position: relative; /* Add relative positioning */
        }

        a:hover {
            background-color: #0058a0;
        }

        .button-container {
            text-align: center;
            display: flex; /* Use flexbox to control layout */
            justify-content: center; /* Center align the buttons horizontally */
        }

        .button-container a {
            margin: 0 10px; /* Add margin to separate the buttons horizontally */
        }

        .center-label {
            position: absolute; /* Absolute positioning to center the label */
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%); /* Center horizontally and vertically */
        }
    </style>
</head>

<body>

<h1> Login Successful </h1>

<%
	// Recupero l'utente dall'oggetto session
	String sessionUser = (String) session.getAttribute("user");
	String rememberedUser = null;
	boolean isRememberMePresent = false;

	Cookie[] cookies = null;
	
	/*
	
		Se non vi � alcuna sessione aperta per nessun utente,
		si cerca nella lista di cookies della richiesta e, se
		viene trovato un cookie valido per tale utente, allora
		la variabile "username" viene valorizzata

	*/

	if (sessionUser == null) {
		
	    cookies = request.getCookies();
	    
	    for (Cookie cookie : cookies) {
	    	
	        if (cookie.getName().equals("rememberMe")) {
	            rememberedUser = Utils.getUsernameFromCookie(cookie.getValue());
	            isRememberMePresent = true;
	            break;
	        }
	        
	    }
	    
	}
	
	if(sessionUser != null || isRememberMePresent) {
	
%>

	    <h3> Benvenuto, <%= sessionUser %> </h3> <br>
	    
        <div class="button-container">
        
            <a href="LogoutServlet">
                <span class="center-label">Logout</span>
            </a>
            
            <a href="FileUploadServlet"> Carica proposta progettuale </a>
            
        </div>
        
        <%
        
		} else
			
			response.sendRedirect("./index.jsp");
		
        %>
        
</body>
</html>
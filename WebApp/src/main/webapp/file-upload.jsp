<%@page import="model.UploadedFile"%>
<%@page import="dao.FileUploadDAO"%>
<%@page import="java.util.List"%>
<%@page import="utils.Utils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Caricamento proposte progettuali</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f0f0;
            margin: 0;
            padding: 0;
        }

        header {
            background-color: #2779e2;
            color: #fff;
            text-align: center;
            padding: 20px 0;
        }

        h1 {
            margin: 0;
        }

        .container {
            width: 80%;
            margin: 20px auto;
            background-color: #fff;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 5px;
            position: relative;
        }

        input[type="file"] {
            margin: 10px 0;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        table, th, td {
            border: 1px solid #ddd;
        }

        th, td {
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #2779e2;
            color: #fff;
        }

        .logout-button {
            background-color: #2779e2;
            color: #fff;
            border: none;
            padding: 10px 20px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            border-radius: 5px;
            cursor: pointer;
            position: absolute;
            top: 10px;
            right: 10px;
        }

        .logout-button:hover {
            background-color: #1a5dab;
        }
    </style>
</head>
<body>
    <header>
        <h1>Caricamento di proposte progettuali</h1>
    </header>
    <div class="container">
        <h2>Carica qui i tuoi file</h2>
        <form action="FileUploadServlet" method="POST" enctype="multipart/form-data">
            <input type="file" name="file" id="file">
            <input type="submit" value="Invia file">
        </form>
        
        <form action="LogoutServlet" method="POST">
            <input type="submit" value="Logout" class="logout-button">
        </form>

        <h2>Elenco dei file caricati per utente</h2>
        
        <table>
            <tr>
                <th>Utente</th>
                <th>Nome file</th>
            </tr>
            
            <%
            
            List<UploadedFile> uploadedFiles = (List<UploadedFile>) request.getAttribute("uploadedFiles");
            
            for (UploadedFile file : uploadedFiles) {
            	
            %>
            
            <tr>
                <td><%= file.getUsername() %></td>
                <td><a href="FileDownloadServlet?username=<%= file.getUsername() %>&id=<%= file.getId() %>"><%= file.getFileName() %></a></td>
            </tr>
            
            <%
            
            }
            
            %>
            
        </table>
        
        			<%
			
			String errorMessage = (String) session.getAttribute("errorMessage");
				            		
			if(errorMessage != null && errorMessage.length() != 0) {
			
			%>
			
			<p id="error" style="text-align: center; color: red"><%= errorMessage %></p>
			
			<%
			
			}
			
			session.setAttribute("errorMessage", "");
			
			%>
    </div>
</body>
</html>

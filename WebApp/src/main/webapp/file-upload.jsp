<%@page import="utils.Utils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>File Upload and User Table</title>
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

        <h2>Elenco dei file caricati per utente</h2>
        <table>
            <tr>
                <th>Utente</th>
                <th>Nome file</th>
            </tr>
            
            <%
            String user = (String) session.getAttribute("user");
            String fileFromUser = (String) session.getAttribute("uploadedFileName");
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
                // Check if the user has uploaded a file, and if so, add it to the table
                // String uploadedFileName = request.getParameter("file");
               if (fileFromUser != null) {
            %>
			 <tr>
                <td><%= user %></td>
                <td><%= fileFromUser %></td>
            </tr>
            <%
               }
            } else {
                response.sendRedirect("./index.jsp");
            }
            %>
        </table>
    </div>
</body>
</html>
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
            background-color: #333;
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
            background-color: #333;
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
        <form action="upload.jsp" method="post" enctype="multipart/form-data">
            <input type="file" name="file" id="file">
            <input type="submit" value="Upload File">
        </form>

        <h2>User Table</h2>
        
        <table>
	        
        
            <tr>
            
                <th>User</th>
                <th>File Uploaded by This User</th>
                
            </tr>
            
            <tr>
            
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
            
            if(user != null) {
            
            %>
				
				<td> <%= user %> </td>                
                <td>file1.txt</td>
                
            </tr>
            
            <tr>
            
            <%
            
            } else {
            	
            	response.sendRedirect("./index.jsp");
            }
            
            %>
                <td>User 2</td>
                <td>file2.png</td>
            </tr>
            
            
        </table>
        
        
        
    </div>
</body>
</html>
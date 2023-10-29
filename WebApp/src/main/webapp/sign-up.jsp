<!DOCTYPE html>
<%@page import="model.User"%>
<%@page import="java.util.List"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Registrazione</title>

<link rel="stylesheet" href="style.css">

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>

</head>
<body>

<section class="vh-100" style="background-color: #2779e2;">
  <div class="container h-100">
    <div class="row d-flex justify-content-center align-items-center h-100">
      <div class="col-xl-9">

        <h1 class="text-white mb-4">Registrati</h1>

		<form action="sign-up" method="POST" enctype="multipart/form-data">

        <div class="card" style="border-radius: 15px;">
          <div class="card-body">

            <div class="row align-items-center pt-4 pb-3">
            
              <div class="col-md-3 ps-5">

                <h6 class="mb-0">Username</h6>

              </div>
              
              <div class="col-md-9 pe-5">

                <input type="text" name="username" class="form-control form-control-lg" placeholder="test" required />

              </div>
            </div>

            <hr class="mx-n3">

            <div class="row align-items-center py-3">
            
              <div class="col-md-3 ps-5">

                <h6 class="mb-0">Password</h6>

              </div>
              
              <div class="col-md-9 pe-5">

                <input type="password" name="password" class="form-control form-control-lg" required />

              </div>
            </div>

            <hr class="mx-n3">
            
                        <div class="row align-items-center py-3">
            
              <div class="col-md-3 ps-5">

                <h6 class="mb-0">Conferma password</h6>

              </div>
              
              <div class="col-md-9 pe-5">

                <input type="password" name="confirm_password" class="form-control form-control-lg" required />

              </div>
            </div>
            
            <hr class="mx-n3">

            <div class="row align-items-center py-3">
            
            </div>


            <div class="row align-items-center py-3">
              <div class="col-md-3 ps-5">

                <h6 class="mb-0">Immagine del profilo (opzionale)</h6>

              </div>
              <div class="col-md-9 pe-5">

                <input class="form-control form-control-lg" name="photo" id="formFileLg" type="file"/>

              </div>
            </div>

            <hr class="mx-n3">

            <div class="px-5 py-4">
              <button type="submit" class="btn btn-primary btn-lg">Registrati</button>
            </div>
            
            <%
			
			String errorMessage = (String) session.getAttribute("errorMessage");
				            		
			if(errorMessage != null && errorMessage.length() != 0) {
			
			%>
			
			<p id="error" style="text-align: center; color: red"><%= errorMessage %></p>
			
			<%
			
			}
			
			session.setAttribute("errorMessage", "");
			session.invalidate();
			
			%>
			
          </div>
        </div>

		</form>
		
      </div>
    </div>
  </div>
</section>

</body>
</html>
<!DOCTYPE html>
<%@page import="model.User"%>
<%@page import="java.util.List"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>

<link rel="stylesheet" href="style.css">

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>

</head>
<body>

<section class="vh-100" style="background-color: #2779e2;">
  <div class="container h-100">
    <div class="row d-flex justify-content-center align-items-center h-100">
      <div class="col-xl-9">

        <h1 class="text-white mb-4">Accedi</h1>

		<form action="SignInServlet" method="POST">

        <div class="card" style="border-radius: 15px;">
          <div class="card-body">

            <div class="row align-items-center pt-4 pb-3">
              <div class="col-md-3 ps-5">

                <h6 class="mb-0">E-mail</h6>

              </div>
              <div class="col-md-9 pe-5">

                <!-- <input type="email" class="form-control form-control-lg" name = "email" placeholder="example@example.com" required />  -->
				<input type="text" class="form-control form-control-lg" name = "email" placeholder="example@example.com" />

              </div>
            </div>

            <hr class="mx-n3">

            <div class="row align-items-center py-3">
              <div class="col-md-3 ps-5">

                <h6 class="mb-0">Password</h6>

              </div>
              <div class="col-md-9 pe-5">

                <input type="password" name = "password" class="form-control form-control-lg"/>

              </div>
            </div>

            <hr class="mx-n3">

            <div class="row align-items-center py-3">
            
            <!-- 
            
              <div class="col-md-3 ps-5">
                <h6 class="mb-0">Full name</h6>
              </div>
            
              <div class="col-md-9 pe-5">
                <textarea class="form-control" rows="3" placeholder="Message sent to the employer"></textarea>
              </div>
              
            <hr class="mx-n3">
            
            <div class="row align-items-center py-3">
              <div class="col-md-3 ps-5">
                <h6 class="mb-0">Upload CV</h6>
              </div>
              <div class="col-md-9 pe-5">
                <input class="form-control form-control-lg" id="formFileLg" type="file" />
              </div>
            </div>
            
            <hr class="mx-n3">
            
             -->
              
            </div>

            <div class="px-5 py-4">
              <button type="submit" class="btn btn-primary btn-lg">Login</button>
            </div>
            
            <p> Non hai un account? <a href="sign-up"> Registrati </a> </p>

          </div>
        </div>
        
        </form>
        
        <p style="color:white;"> Test </p> <br>
        
    </div>
  </div>
</section>

</body>
</html>
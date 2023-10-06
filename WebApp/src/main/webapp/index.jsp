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

                <h6 class="mb-0">Username</h6>

              </div>
              <div class="col-md-9 pe-5">

                <!-- <input type="email" class="form-control form-control-lg" name = "email" placeholder="example@example.com" required />  -->
				<input type="text" class="form-control form-control-lg" name = "username" placeholder="test" required />

              </div>
            </div>

            <hr class="mx-n3">

            <div class="row align-items-center py-3">
              <div class="col-md-3 ps-5">

                <h6 class="mb-0">Password</h6>

              </div>
              <div class="col-md-9 pe-5">

                <input type="password" name = "password" class="form-control form-control-lg" required />

              </div>
            </div>
            
            <hr class="mx-n3">
            
            <!-- REMEMBER ME -->
            
            <div class="row align-items-center py-3">
				    <div class="col-md-9 pe-5">
				        <div class="form-check">
				            <input type="checkbox" class="form-check-input" id="rememberMe" name="rememberme">
				            <label class="form-check-label" for="rememberMe">Remember me</label>
				        </div>
				    </div>
			</div>

            <div class="row align-items-center py-3">
            
            </div>

            <div class="px-5 py-4">
              <button type="submit" class="btn btn-primary btn-lg">Login</button>
            </div>
            
            <p> Non hai un account? <a href="sign-up"> Registrati </a> </p>

          </div>
        </div>
        
        </form>
        
    </div>
  </div>
</section>

</body>
</html>
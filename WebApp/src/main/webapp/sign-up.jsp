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

        <h1 class="text-white mb-4">Registrati</h1>

		<form action="SignUpServlet" method="POST">

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
            
            <!-- 
            
              <div class="col-md-3 ps-5">
                <h6 class="mb-0">Full name</h6>
              </div>
            
              <div class="col-md-9 pe-5">
                <textarea class="form-control" rows="3" placeholder="Message sent to the employer"></textarea>
              </div>
              
            <hr class="mx-n3">
            
             -->
            
            
              
            </div>


            <div class="row align-items-center py-3">
              <div class="col-md-3 ps-5">

                <h6 class="mb-0">Immagine del profilo (opzionale)</h6>

              </div>
              <div class="col-md-9 pe-5">

                <input class="form-control form-control-lg" name="photo" id="formFileLg" type="file" />
                <!-- <div class="small text-muted mt-2">Upload your CV/Resume or any other relevant file. Max file size 50 MB</div>  -->

              </div>
            </div>

            <hr class="mx-n3">

            <div class="px-5 py-4">
              <button type="submit" class="btn btn-primary btn-lg">Registrati</button>
            </div>
            
          </div>
        </div>

		</form>
		
		<ol>
        
        <%
        
        	
        
        	//List<User> users = (List<User>) request.getAttribute("usersList");
        	
        	//if(users != null) {
        		
            //	for(User user : users) {
            		
            	//	out.println( "<li style='color:white;'> " + user.toString() + " </li>" );
            		
            	//}
        		
        	//}
        
        %>

        </ol>
		
      </div>
    </div>
  </div>
</section>

</body>
</html>
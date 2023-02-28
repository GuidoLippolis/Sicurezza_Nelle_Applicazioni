package servlets;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;
import model.User;

/**
 * Servlet implementation class SignUpServlet
 */
@WebServlet("/sign-up")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.getRequestDispatcher("sign-up.jsp").forward(request, response);
		
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*
		 * FASE DI REGISTRAZIONE:
		 * 
		 * 1. L'utente invia le credenziali scelte: email, password ed eventualmente foto profilo
		 * 2. Le credenziali vengono recuperate dall'oggetto request
		 * 3. Viene chiamato un metodo in JDBCLoginDao che permette di inserire le credenziali scelte nel database
		 * 
		 * */
		
		String email = request.getParameter("email");
		byte[] password = request.getParameter("password").getBytes();
		byte[] photo = request.getParameter("photo").getBytes();

		User user = new User(email, photo);
		
		boolean insertedUser = false;
		
		try {
			
			if(email.length() != 0 && password.length != 0)
				insertedUser = UserDAO.signIn(user, password);
				
			
			if(insertedUser)
				System.out.println("Transazione avvenuta con successo");
			else
				System.out.println("Successo senza succ");
			
		} catch (SQLException | NoSuchAlgorithmException | ClassNotFoundException e) {
			
			e.printStackTrace();
			
		}
		
		doGet(request, response);
		
	}

}

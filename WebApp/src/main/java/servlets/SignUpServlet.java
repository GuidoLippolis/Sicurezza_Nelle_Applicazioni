package servlets;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

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
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			
			List<User> users = UserDAO.findByUsername(request.getParameter("username"));
			
			request.setAttribute("usersList", users);
			
		} catch (SQLException | ClassNotFoundException e) {

			e.printStackTrace();
			
		}
		
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
		 * 3. Viene chiamato un metodo in UserDAO che permette di inserire le credenziali scelte nel database
		 * 
		 * */
		
		String username = request.getParameter("username");
		byte[] password = request.getParameter("password").getBytes();
		byte[] passwordToConfirm = request.getParameter("confirm_password").getBytes();
		byte[] photo = request.getParameter("photo").getBytes();

		boolean insertedUser = false;
		
		try {
			
			if(Arrays.equals(password, passwordToConfirm))
				insertedUser = UserDAO.signUp(new User(username, photo), password);
			
			if(insertedUser) {
				
				/*
				 * TODO: Sostituire con log
				 * 
				 * */
				
				System.out.println("Inserimento avvenuto con successo");
				
			} else {
				
				System.out.println("Inserimento annullato");
				response.sendRedirect("./index.jsp");
				return;
				
			}
			
		} catch (SQLException | NoSuchAlgorithmException | ClassNotFoundException e) {
			
			e.printStackTrace();
			
		}
		
		doGet(request, response);
		
	}

}

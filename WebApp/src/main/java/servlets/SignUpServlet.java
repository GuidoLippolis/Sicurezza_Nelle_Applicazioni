package servlets;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import dao.UserDAO;
import model.User;

/**
 * Servlet implementation class SignUpServlet
 */
@WebServlet("/sign-up")
public class SignUpServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(SignUpServlet.class);
	
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
			
			if(insertedUser) 
				
				log.info("Registrazione avvenuta con successo");
				
			 else {
				
				log.info("Errore nella registrazione");
				
				response.sendRedirect("./index.jsp");
				
				return;
				
			}
			
		} catch (SQLException | NoSuchAlgorithmException | ClassNotFoundException e) {
			
			log.info("Errore in SignUpServlet: ", e);
			e.printStackTrace();
			
		}
		
		doGet(request, response);
		
	}

}

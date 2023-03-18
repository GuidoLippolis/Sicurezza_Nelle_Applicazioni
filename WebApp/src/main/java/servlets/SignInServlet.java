package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import dao.UserDAO;
import model.User;

/**
 * Servlet implementation class SignInServlet
 */
public class SignInServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(SignUpServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignInServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		byte[] password = request.getParameter("password").getBytes();
		
		boolean logged = false;
		
		try {
			
			logged = UserDAO.signIn(new User(username), password);
			
			if(logged) {
				
				log.info("Login effettuato con successo");
				
				HttpSession oldSession = request.getSession(false);
				
				if(oldSession != null)
					oldSession.invalidate();
				
				HttpSession currentSession = request.getSession();
				
				currentSession.setAttribute("user", username);
				
				currentSession.setMaxInactiveInterval(60);
				
				response.sendRedirect("./success.jsp");
				
				return;
				
			} else {
				
				log.info("Errore durante il login");
				
				response.sendRedirect("./index.jsp");
				
				return;
				
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			
			log.info("Errore in SignInServlet: ", e);
			e.printStackTrace();
			
		}
		
		doGet(request, response);
	
	}

}

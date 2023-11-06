package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import dao.CookieDAO;
import net.jcip.annotations.ThreadSafe;

/**
 * Servlet implementation class LogoutServlet
 */
@ThreadSafe
public class LogoutServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(LogoutServlet.class);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*
		 * Recupero della sessione corrente. Il parametro "false"
		 * serve a fare in modo che, se non è presente una sessione,
		 * non dev'esserne creata un'altra
		 * 
		 * */
		
		HttpSession currentSession = request.getSession(false);
		
		// Se vi è una sessione aperta, questa viene chiusa
		if(currentSession != null)
			currentSession.invalidate();
		
		boolean deletedRememberMeCookie = false;
		
        Cookie[] cookies = request.getCookies();
        
        // Cancellazione dell'eventuale Cookie "rememberMe"
        try {
			
            if (cookies != null) {
            	
                for (Cookie cookie : cookies) {
                	
                    if ("rememberMe".equals(cookie.getName())) {
                    	
                    	String encryptedCookieFromDB = CookieDAO.findCookieByValue(CookieDAO.findCookieByValue(cookie.getValue()));
                    	
                    	deletedRememberMeCookie = CookieDAO.deleteCookieByValue(encryptedCookieFromDB);
                    	
                        cookie.setMaxAge(0);
                        
                        response.addCookie(cookie);
                        
                        log.info(deletedRememberMeCookie ? "Il cookie è stato cancellato correttamente dal database" : "Il cookie è ancora nel database");
                        
                    }
                }
            }
        	
		} catch (Exception e) {

			log.error("Eccezione in LogoutServlet: ", e);

		}
		
        log.info("Logout effettuato con successo");
        
		response.sendRedirect("sign-in");
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
		
	}

}

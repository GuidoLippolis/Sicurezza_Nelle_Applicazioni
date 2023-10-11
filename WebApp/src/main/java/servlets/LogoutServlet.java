package servlets;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import dao.CookieDAO;
import enumeration.PropertiesKeys;
import utils.ApplicationPropertiesLoader;
import utils.EncryptionUtils;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(LogoutServlet.class);
	
	private static Properties prop = ApplicationPropertiesLoader.getProperties();
       
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
		
		HttpSession currentSession = request.getSession(false);
		
		if(currentSession != null)
			currentSession.invalidate();
		
		boolean deletedCookie = false;
		
        Cookie[] cookies = request.getCookies();
        
        try {
			
            if (cookies != null) {
            	
                for (Cookie cookie : cookies) {
                	
                    if ("rememberMe".equals(cookie.getName())) {
                    	
                    	String encryptedCookieFromDB = CookieDAO.findCookieByValue(new EncryptionUtils(prop.getProperty(PropertiesKeys.PASSPHRASE.toString())).encrypt(cookie.getValue()) );
                    	
                    	deletedCookie = CookieDAO.deleteCookieByValue(encryptedCookieFromDB);
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                        
                        log.info(deletedCookie ? "Il cookie è stato cancellato correttamente dal database" : "Il cookie NON è stato cancellato correttamente dal database");
                        
                    }
                }
            }
        	
		} catch (Exception e) {

			log.error("Eccezione in LogoutServlet: ", e);

		}
		
		response.sendRedirect("./index.jsp");
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
		
	}

}

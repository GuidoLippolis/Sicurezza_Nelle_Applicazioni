package servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import dao.CookieDAO;
import dao.UserDAO;
import enumeration.PropertiesKeys;
import model.User;
import utils.ApplicationPropertiesLoader;
import utils.EncryptionUtils;
import utils.Utils;

/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet("/file-upload")
public class FileUploadServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(FileUploadServlet.class);
	
	private static Properties prop = ApplicationPropertiesLoader.getProperties();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileUploadServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		boolean deletedCookie = false;
		
		try {
			
	        Cookie[] cookies = request.getCookies();
	        
	        /*
	         * Prima di verificare che le credenziali dell'utente 
	         * 
	         * */
	        
	        if (cookies != null) {
	        	
	            for (Cookie cookie : cookies) {
	            	
	                if (cookie.getName().equals("rememberMe")) {
	                	
	                	/*
	                	 * Quando l'utente raggiunge l'applicazione, si controlla se
	                	 * esso è già in possesso di un Cookie. Se tale condizione è
	                	 * verificata, allora l'utente viene autenticato senza la
	                	 * necessità di fornire le credenziali di accesso
	                	 * 
	                	 * */

	                	String cookieValueFromDB = CookieDAO.findCookieByValue(new EncryptionUtils(prop.getProperty(PropertiesKeys.PASSPHRASE.toString())).encrypt(cookie.getValue()) );

	                	/*
	                	 * Viene recuperata la data di scadenza (in secondi) dal database sulla base del valore del cookie.
	                	 * Se il cookie esiste nel database e, allo stesso tempo, non è scaduto, allora l'utente viene
	                	 * autenticato con successo. Altrimenti, il cookie viene cancellato dal database
	                	 * 
	                	 * */
	                	
	                	long expirationDateForCookie = CookieDAO.findExpirationDateByCookieValue(cookieValueFromDB);
	                	
	                	if(cookieValueFromDB != null && Utils.isCookieValid(System.currentTimeMillis(), expirationDateForCookie)) {
	                		
	                		response.sendRedirect("./file-upload.jsp");
	                		return;
	                		
	                	} else {
	                		
	                		deletedCookie = CookieDAO.deleteCookieByValue(cookieValueFromDB);
	                		response.sendRedirect("./index.jsp");
	                		return;
	                		
	                	}
	                	
	                }
	                
	            }
	            
	        }
	        
		} catch (Exception e) {

			log.error("Eccezione in SignInServlet: ", e);
			
		}
		
		request.getRequestDispatcher("file-upload.jsp").forward(request, response);
		
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
    	
    	
        doGet(request, response);
        
    }

}

package servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import dao.CookieDAO;
import dao.UserDAO;
import model.User;
import utils.EncryptionUtils;
import utils.Utils;

/**
 * Servlet implementation class SignInServlet
 */
@WebServlet("/sign-in")
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
	                	 * L'utente invia il cookie in chiaro
	                	 * Il metodo findCookieByValue() cripta ciò che manda l'utente e cerca corrispondenza nel DB
	                	 * Restituisco ciò che trovo nel DB e lo decripto
	                	 * Se il valore decriptato è uguale a quello che ha inviato il client, l'utente viene autenticato
	                	 * 
	                	 * */
	                	
	                	String foundCookie = CookieDAO.findCookieByValue(EncryptionUtils.encrypt(cookie.getValue(), "secret"));
	                	
	                	String test = Base64.getEncoder().encodeToString(foundCookie.getBytes(StandardCharsets.UTF_8));
	                	
	                	String decryptedCookieFromDB = EncryptionUtils.decrypt(foundCookie);
	                	
	                	if(decryptedCookieFromDB.equals(cookie.getValue()))
	                		log.info("Cookie valido");
	                	else
	                		log.info("Cookie NON valido");
	                	
	                }
	                
	            }
	        }
	        
		} catch (Exception e) {

			log.error("Eccezione in SignInServlet: ", e);
			
		}
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		byte[] password = request.getParameter("password").getBytes();
		boolean rememberMe = "on".equals(request.getParameter("rememberme"));
		
		boolean loggedUser = false;
		boolean savedCookie = false;
		
		try {
			
			loggedUser = UserDAO.signIn(new User(username), password);
			
			if(loggedUser) {
				
				/*
				 * Se l'utente ha selezionato l'opzione "Remember Me" al momento
				 * del login, viene creato un nuovo Cookie per l'utente il cui
				 * nome è una stringa randomica derivante dallo username dell'utente
				 * stesso. Viene settata la durata massima del Cookie e quest'ultimo
				 * viene aggiunto alla risposta del server
				 * 
				 * */
				
				if(rememberMe) {
					
					Cookie rememberMeCookie = new Cookie("rememberMe", Utils.generateRandomToken(username));
					
					rememberMeCookie.setMaxAge(15 * 60);
					
					response.addCookie(rememberMeCookie);

					User user = UserDAO.findByUsername(username);
					
					String encryptedCookie = EncryptionUtils.encrypt(rememberMeCookie.getValue(), "secret");
					
					savedCookie = CookieDAO.saveCookie(encryptedCookie, user);
					
					log.info(savedCookie ? "Cookie memorizzato correttamente nel database" : "Errore: il Cookie non è stato memorizzato correttamente nel database");
					
				}
				
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
			
		} catch (Exception e) {
			
			log.error("Eccezione in SignInServlet: ", e);
			e.printStackTrace();
			
		}
		
		doGet(request, response);
	
	}

}

package servlets;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import constants.Constants;
import dao.CookieDAO;
import dao.UserDAO;
import exception.LoginFailedException;
import model.User;
import net.jcip.annotations.ThreadSafe;
import utils.ApplicationPropertiesLoader;
import utils.EncryptionUtils;
import utils.PasswordUtils;
import utils.Utils;

/**
 * Servlet implementation class SignInServlet
 */
@ThreadSafe
public class SignInServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(SignInServlet.class);
	
	private static Properties prop = ApplicationPropertiesLoader.getProperties();
       
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

		boolean isRememberMeCookieExpired = true;
		boolean deletedRememberMeCookie = false;
		
    	HttpSession session = request.getSession(false);
    	
    	String sessionUser = null;
    	
    	if(session != null)
    		sessionUser = (String) request.getSession(false).getAttribute("user");
		
		Cookie[] cookies = request.getCookies();
		
		try {
			
			/*
			 * Quando cerco di raggiungere la rotta /sign-in, controllo:
			 * 
			 * - Se c'è un Cookie "rememberMe" e questo non è scaduto, l'attributo user della sessione
			 *   corrente viene valorizzato con lo username dell'utente attualmente loggato
			 * 
			 * - Se non c'è un Cookie "rememberMe", ma vi è una sessione aperta, l'utente viene
			 *   reindirizzato alla pagina di benvenuto
			 *   
			 * - Altrimenti, viene reindirizzato alla pagina di login
			 * 
			 * */
			
			if(cookies != null) {
				
				for(Cookie cookie : cookies) {
					
					if(cookie.getName().equals("rememberMe")) {
						
						isRememberMeCookieExpired = Utils.isCookieExpired(CookieDAO.findCookieExpirationTimeByCookieValue(cookie.getValue()));
						
						if(isRememberMeCookieExpired) {
							
							deletedRememberMeCookie = CookieDAO.deleteCookieByValue(cookie.getValue());
							
							log.info(deletedRememberMeCookie ? "Il cookie è stato cancellato correttamente dal database" : "Il cookie è ancora nel database");
							
							request.getRequestDispatcher("index.jsp").forward(request, response);
							return;
							
						} else {
							
							log.info(deletedRememberMeCookie ? "Il cookie è stato cancellato correttamente dal database" : "Il cookie è ancora nel database");
							
							request.getSession().setAttribute("user", CookieDAO.findUsernameByCookieValue(cookie.getValue()));
							request.getRequestDispatcher("./success.jsp").forward(request, response);
							return;
							
						}
					
					}
					
					
				}
				
			}
			
		} catch (Exception e) {
			
			log.error("Eccezione in SignInServlet: " + e.getMessage());
			
		}
		
		if(sessionUser != null || !isRememberMeCookieExpired)
		
			request.getRequestDispatcher("welcome").forward(request, response);
		
		else
			
			request.getRequestDispatcher("index.jsp").forward(request, response);
	
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		
		byte[] password = request.getParameter("password").getBytes();
		
		boolean rememberMe = "on".equals(request.getParameter("rememberMe"));
		boolean loggedUser = false;
		boolean savedCookie = false;
		
		try {
			
			/*
			 * Il metodo signIn() verifica l'esistenza nel database di un utente con
			 * lo username e la password forniti in input. Se l'utente è presente,
			 * viene aperta una nuova sessione HTTP
			 * 
			 * */
			
			loggedUser = UserDAO.signIn(username, password);
			
			if(loggedUser) {
				
				HttpSession oldSession = request.getSession(false);
				
				if(oldSession != null)
					oldSession.invalidate();
				
				HttpSession currentSession = request.getSession();
				
				currentSession.setMaxInactiveInterval(60 * 15);
				
				currentSession.setAttribute("user", username);
				
				/*
				 * Se l'utente ha selezionato l'opzione "Remember Me" al momento
				 * del login, viene creato un nuovo Cookie per l'utente il cui
				 * nome è una stringa randomica derivante dallo username dell'utente
				 * stesso. Viene settata la durata massima del Cookie e quest'ultimo
				 * viene aggiunto alla risposta del server
				 * 
				 * */
				
				if(rememberMe) {
					
					// GESTIONE DEI COOKIE
					
					// Generazione del valore casuale del Cookie derivante allo username
					String randomCookieValue = username + "#" + Utils.generateRandomToken(20);
					
					// Crittografia simmetrica del valore del Cookie
					String passphrase = System.getenv(Constants.PASSPHRASE);
					String encryptedCookieValue = new EncryptionUtils(passphrase).encrypt(randomCookieValue);
					
					Cookie rememberMeCookie = new Cookie("rememberMe", encryptedCookieValue);
					
					// Setting della durata massima del Cookie in secondi
					rememberMeCookie.setMaxAge(Utils.calculateMinutesFromNow(15));
					
					// Setting di attributi di sicurezza
					rememberMeCookie.setHttpOnly(true);
					rememberMeCookie.setSecure(true);
			
					savedCookie = CookieDAO.saveCookie(encryptedCookieValue, rememberMeCookie.getMaxAge(), username);
					
					response.addCookie(rememberMeCookie);
					
					log.info(savedCookie ? "Cookie memorizzato correttamente nel database" : "Errore: il Cookie NON è stato memorizzato correttamente nel database");
					
				}
				
			}
			
			if(loggedUser) {
				
				log.info("Login effettuato con successo");
				
				response.sendRedirect("welcome");
				
				return;
				
			} else {

				throw new LoginFailedException();
				
			}
			
		} catch (ClassNotFoundException | SQLException | InvalidKeyException
				| NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			
			log.error("Eccezione in SignInServlet: " + e.getMessage());
			
		} catch (LoginFailedException e) {
			
			log.error("Eccezione in SignInServlet: " + e.getMessage());
			
			request.setAttribute("errorMessage", e.getMessage());

			request.getRequestDispatcher("./index.jsp").forward(request, response);

			return;
			
		} finally {
			
			PasswordUtils.clearArray(password);
			
		}
		
		doGet(request, response);
	
	}

}

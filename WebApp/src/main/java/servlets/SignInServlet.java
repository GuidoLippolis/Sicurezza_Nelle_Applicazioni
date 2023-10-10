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
import dao.UserDAO;
import enumeration.PropertiesKeys;
import model.User;
import utils.ApplicationPropertiesLoader;
import utils.EncryptionUtils;
import utils.Utils;

/**
 * Servlet implementation class SignInServlet
 */
@WebServlet("/sign-in")
public class SignInServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(SignUpServlet.class);
	
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

		boolean isCookieValid = false;
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
	                	 * 1. Recupero dell'ID dell'utente del database dal valore del Cookie
	                	 * 2. Esecuzione di una query mirata a recuperare l'utente tramite l'ID nella tabella dei cookies
	                	 * 3. Calcolo della data di scadenza e confronto di quest'ultima con la data corrente
	                	 * 
	                	 * */

	                	String cookieValueFromDB = CookieDAO.findCookieByValue(EncryptionUtils.encrypt(cookie.getValue(), prop.getProperty(PropertiesKeys.PASSPHRASE.toString())));

	                	System.out.println("cookie from db = " + cookieValueFromDB);
	                	
	                	/*
	                	 * TODO: Implementare controllo di scadenza password (con cancellazione del cookie dal database)
	                	 * 
	                	 * */
	                	
	                	if(cookieValueFromDB != null && isCookieValid) {
	                		
	                		response.sendRedirect("./success.jsp");
	                		return;
	                		
	                	} else {
	                		
	                		deletedCookie = CookieDAO.deleteCookieByValue(cookieValueFromDB);
	                		System.out.println("cookie cancellato = " + deletedCookie);
	                		response.sendRedirect("./index.jsp");
	                		return;
	                		
	                	}
	                	
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
				
				// Sessione
				
				HttpSession oldSession = request.getSession(false);
				
				if(oldSession != null)
					oldSession.invalidate();
				
				HttpSession currentSession = request.getSession();
				
				currentSession.setMaxInactiveInterval(60);
				
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
					
					/*
					 * GESTIONE DEI COOKIE:
					 * 
					 * - Recupero l'utente tramite lo username
					 * 
					 * - Genero un token derivante dallo username, al quale concateno una stringa casuale
					 * 
					 * - La stringa casuale ha il seguente pattern: username#randomstring@@@userid@@@. In questo
					 *   modo, quando l'utente raggiungerà nuovamente l'applicazione, tramite il metodo
					 *   getUserIdFromCookie viene eseguita una query mirata al database per recuperare
					 *   il cookie associato all'utente. Se viene trovato un record nel database e se il cookie
					 *   in questione non è scaduto, l'utente raggiunge la pagina success.jsp, altrimenti viene
					 *   reindirizzato alla pagina di login
					 * 
					 * */
					
					User user = UserDAO.findByUsername(username);
					
					String randomCookieValue = Utils.generateRandomToken(username, 20);
					
					Cookie rememberMeCookie = new Cookie("rememberMe", randomCookieValue);
					
					rememberMeCookie.setMaxAge(600000000);

					savedCookie = CookieDAO.saveCookie(EncryptionUtils.encrypt(rememberMeCookie.getValue(), prop.getProperty(PropertiesKeys.PASSPHRASE.toString())), rememberMeCookie.getMaxAge(), user);
					
					response.addCookie(rememberMeCookie);
					
					log.info(savedCookie ? "Cookie memorizzato correttamente nel database" : "Errore: il Cookie non è stato memorizzato correttamente nel database");
					
				}
				
				log.info("Login effettuato con successo");
				
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

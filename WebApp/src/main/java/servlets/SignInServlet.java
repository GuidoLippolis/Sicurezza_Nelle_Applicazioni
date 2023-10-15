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

		boolean deletedCookie = false;
		
		try {
			
	        Cookie[] cookies = request.getCookies();
	        
	        /*
	         * Prima di verificare che le credenziali dell'utente siano corrette, viene verificata la
	         * presenza di un cookie chiamato "rememberMe". Se presente, viene letto il suo valore e
	         * quest'ultimo viene criptato, in modo tale da eseguire una query mirata al database filtrando
	         * il cookie per valore (essendo stato memorizzato criptato nel database).
	         * 
	         * */
	        
	        if (cookies != null) {
	        	
	            for (Cookie cookie : cookies) {
	            	
	                if (cookie.getName().equals("rememberMe")) {
	                	
	                	String cookieValueFromDB = CookieDAO.findCookieByValue(new EncryptionUtils(prop.getProperty(PropertiesKeys.PASSPHRASE.toString())).encrypt(cookie.getValue()) );

	                	/*
	                	 * Viene recuperata la data di scadenza (in secondi) dal database sulla base del valore del cookie.
	                	 * Se il cookie esiste nel database e, allo stesso tempo, non è scaduto, allora l'utente viene
	                	 * autenticato con successo. Altrimenti, il cookie viene cancellato dal database
	                	 * 
	                	 * */
	                	
	                	long expirationDateForCookie = CookieDAO.findExpirationDateByCookieValue(cookieValueFromDB);
	                	
	                	if(!cookieValueFromDB.isBlank() || (cookieValueFromDB != null) && Utils.isCookieValid(System.currentTimeMillis(), expirationDateForCookie)) {
	                		
	                		response.sendRedirect("./success.jsp");
	                		return;
	                		
	                	} else {
	                		
	                		deletedCookie = CookieDAO.deleteCookieByValue(cookieValueFromDB);
	                		
	                		log.info(deletedCookie ? "Il cookie è stato cancellato correttamente dal database" : "Il cookie NON è stato cancellato correttamente dal database");
	                		
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
			
			/*
			 * Il metodo signIn() verifica l'esistenza nel database di un utente con
			 * lo username e la password forniti in input. Se l'utente è presente,
			 * viene aperta una nuova sessione HTTP
			 * 
			 * */
			
			loggedUser = UserDAO.signIn(new User(username), password);
			
			HttpSession oldSession = request.getSession(false);
			
			if(oldSession != null)
				oldSession.invalidate();
			
			HttpSession currentSession = request.getSession();
			
			currentSession.setMaxInactiveInterval(60 * 10);
			
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
				 *   modo, quando l'utente raggiungerà l'applicazione e proverà a caricare un file di proposta
				 *   progettuale, verrà estrapolato lo username tramite il metodo getUsernameFromCookie, il
				 *   quale verrà memorizzato nel database e associato al nome del file relativo alla proposta
				 *   progettuale
				 * 
				 * */
				
				User user = UserDAO.findByUsername(username);
				
				String randomCookieValue = Utils.generateRandomToken(username, 20) + "@@@" + user.getId() + "@@@";
				
				Cookie rememberMeCookie = new Cookie("rememberMe", randomCookieValue);
				
				rememberMeCookie.setMaxAge(600000000);

				savedCookie = CookieDAO.saveCookie(new EncryptionUtils(prop.getProperty(PropertiesKeys.PASSPHRASE.toString())).encrypt(rememberMeCookie.getValue()), rememberMeCookie.getMaxAge(), user);
				
				response.addCookie(rememberMeCookie);
				
				log.info(savedCookie ? "Cookie memorizzato correttamente nel database" : "Errore: il Cookie non è stato memorizzato correttamente nel database");
				
			}
			
			if(loggedUser) {
				
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

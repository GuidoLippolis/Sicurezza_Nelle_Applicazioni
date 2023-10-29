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

import dao.CookieDAO;
import dao.UserDAO;
import enumeration.PropertiesKeys;
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

		System.out.println("############### SignInServlet.doGet() ###############");
		
		boolean isRememberMeCookiePresent = false;
		boolean isRememberMeCookieExpired = false;
		boolean deletedRememberMeCookie = false;
		
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
						
						isRememberMeCookiePresent = true;

						isRememberMeCookieExpired = Utils.isCookieExpired(CookieDAO.findCookieExpirationTimeByUserId(Utils.getUserIdFromCookieValue(cookie.getValue())));
						
						if(isRememberMeCookieExpired)
							deletedRememberMeCookie = CookieDAO.deleteCookieByValue(CookieDAO.findCookieByUserId(Utils.getUserIdFromCookieValue(cookie.getValue())));
						
						log.info(deletedRememberMeCookie ? "Il cookie è stato cancellato correttamente dal database" : "Il cookie NON è stato cancellato correttamente dal database");
						
						if(isRememberMeCookiePresent && !isRememberMeCookieExpired) {
							
							request.getSession().setAttribute("user", Utils.getUsernameFromCookie(cookie.getValue()));
							request.getRequestDispatcher("./success.jsp").forward(request, response);
							return;
							
						} else {
							
							request.getRequestDispatcher("index.jsp").forward(request, response);
							return;
						}
						
					
					}
					
					
				}
				
			}
			
		} catch (Exception e) {
			
			log.error("Eccezione in SignInServlet: " + e.getMessage());
			
		}
		
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
			
			loggedUser = UserDAO.signIn(new User(username), password);
			
			PasswordUtils.clearArray(password);
			
			if(loggedUser) {
				
				HttpSession oldSession = request.getSession(false);
				
				if(oldSession != null)
					oldSession.invalidate();
				
				HttpSession currentSession = request.getSession();
				
				currentSession.setMaxInactiveInterval(60 * 15);
				
				currentSession.setAttribute("user", username);
				
			}
			
			
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
				 * - La stringa casuale ha il seguente pattern: username#randomstring@@@user_id@@@. In questo modo, quando
				 *   l'utente raggiungerà l'applicazione e accederà alla pagina per il caricamento di una proposta progettuale,
				 *   verrà estrapolato lo username tramite il metodo getUsernameFromCookie, il quale verrà memorizzato nel
				 *   database e associato al nome del file relativo alla proposta progettuale
				 * 
				 * */
				
				User user = UserDAO.findByUsername(username);
				
				// Generazione del valore casuale del Cookie derivante allo username
				String randomCookieValue = username + "#" + Utils.generateRandomToken(20) + "@@@" + user.getId() + "@@@";
				
				Cookie rememberMeCookie = new Cookie("rememberMe", randomCookieValue);
				
				// Setting della durata massima del Cookie in secondi
				rememberMeCookie.setMaxAge((int) (System.currentTimeMillis() + 15 * 60 * 1000));

				// Crittografia simmetrica del valore del Cookie
				String passphrase = prop.getProperty(PropertiesKeys.PASSPHRASE.toString());
				String encryptedCookieValue = new EncryptionUtils(passphrase).encrypt(rememberMeCookie.getValue());
				
				savedCookie = CookieDAO.saveCookie(encryptedCookieValue, rememberMeCookie.getMaxAge(), user);
				
				response.addCookie(rememberMeCookie);
				
				log.info(savedCookie ? "Cookie memorizzato correttamente nel database" : "Errore: il Cookie NON è stato memorizzato correttamente nel database");
				
			}
			
			if(loggedUser) {
				
				log.info("Login effettuato con successo");
				
				response.sendRedirect("./success.jsp");
				
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
			
		}
		
		doGet(request, response);
	
	}

}

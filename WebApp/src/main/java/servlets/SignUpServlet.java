package servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import dao.CookieDAO;
import dao.UserDAO;
import exception.DifferentPasswordsException;
import exception.ForbiddenFileTypeException;
import exception.UserAlreadyExistsException;
import exception.UsernameNotValidException;
import utils.FileUtils;
import utils.PasswordUtils;
import utils.Utils;

/**
 * Servlet implementation class SignUpServlet
 */
@MultipartConfig(
	    maxFileSize = 1024 * 1024 * 5) // 5 MB
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
						
						isRememberMeCookieExpired = Utils.isCookieExpired(CookieDAO.findCookieExpirationTimeByUserId(Utils.getUserIdFromCookieValue(cookie.getValue())));
						
						if(isRememberMeCookieExpired)
							deletedRememberMeCookie = CookieDAO.deleteCookieByValue(CookieDAO.findCookieByUserId(Utils.getUserIdFromCookieValue(cookie.getValue())));
						
						log.info(deletedRememberMeCookie ? "Il cookie è stato cancellato correttamente dal database" : "Il cookie è ancora nel database");
						
						if(!isRememberMeCookieExpired) {
							
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
		
		Part photo = request.getPart("photo");
		
		InputStream photoInputStream = null;
		
		if(photo != null)
			photoInputStream = photo.getInputStream();
		
		boolean registeredUser = false;
		
		try {
			
			if(!Utils.isUsernameValid(username))
				throw new UsernameNotValidException(username);
			
            String fileName = FileUtils.getFileName(photo);
            
            /*
             * Il metodo transformFileName aggiunge un timestamp al nome del file per evitare duplicati. In questo
             * modo viene gestita la situazione in cui due utenti diversi caricano un file con lo stesso nome
             * 
             * */
            
            if(fileName != null && fileName.length() != 0) {
            	
            	String finalPath = getServletContext().getRealPath("/") + File.separator + Utils.transformFileName(fileName);
            	
            	photo.write(finalPath);
            	
            	if(!FileUtils.isImage(finalPath))
            		throw new ForbiddenFileTypeException(finalPath);
            	
            }
            
			
			/*
			 * Controllo di corrispondenza tra il valore inserito nel campo "password"
			 * e quello nel campo "conferma password"
			 * 
			 * */
			
			if(Arrays.equals(password, passwordToConfirm)) {
				
				registeredUser = UserDAO.signUp(username, photoInputStream, password);
				
				// Pulizia dei dati sensibili
				PasswordUtils.clearArray(passwordToConfirm);
				
			} else
				
				throw new DifferentPasswordsException();
			
			if(registeredUser) {
				
				log.info("Registrazione avvenuta con successo");
				
				response.sendRedirect("sign-in");
				
				return;
				
			}  else 
				
				log.info("Errore nella registrazione");
			
			
		} catch (SQLException | NoSuchAlgorithmException | ClassNotFoundException | SAXException | TikaException e) {
			
			log.error("Errore in SignUpServlet: " + e.getMessage());
			
		} catch (UserAlreadyExistsException e) {
			
			request.setAttribute("errorMessage", e.getMessage());
			log.error("Eccezione in SignUpServlet: " + e.getMessage());
			
		} catch (ForbiddenFileTypeException e) {
			
			request.setAttribute("errorMessage", e.getMessage());
			log.error("Eccezione in SignUpServlet: " + e.getMessage());
			
		} catch (DifferentPasswordsException e) {

			request.setAttribute("errorMessage", e.getMessage());
			log.error("Eccezione in SignUpServlet: " + e.getMessage());
			
		} catch (UsernameNotValidException e) {

			request.setAttribute("errorMessage", e.getMessage());
			log.error("Eccezione in SignUpServlet: " + e.getMessage());
		
		} finally {
			
			photoInputStream.close();
			
		}
		
		doGet(request, response);
		
	}
	
}

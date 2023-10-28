package servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import dao.UserDAO;
import exception.ForbiddenFileTypeException;
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
		
		boolean insertedUser = false;
		
		try {
			
            String fileName = FileUtils.getFileName(photo);
            
            /*
             * Il metodo transformFileName aggiunge un timestamp al nome del file per evitare duplicati. In questo
             * modo viene gestita la situazione in cui due utenti diversi caricano un file con lo stesso nome
             * 
             * */
            
            if(fileName != null && fileName.length() != 0) {
            	
            	String finalPath = getServletContext().getRealPath("/") + File.separator + Utils.transformFileName(fileName);
            	
            	photo.write(finalPath);
            	
            	if(!FileUtils.isImage(finalPath)) {
            		
            		request.getSession(false).setAttribute("errorMessage", "ATTENZIONE! Tipo di file non consentito!");
            		throw new ForbiddenFileTypeException(finalPath);
            		
            	}
            	
            }
            
			
			/*
			 * Controllo di corrispondenza tra il valore inserito nel campo "password"
			 * e quello nel campo "conferma password"
			 * 
			 * */
			
			if(Arrays.equals(password, passwordToConfirm)) {
				
				insertedUser = UserDAO.signUp(username, photoInputStream, password);
				
				// Pulizia dei dati sensibili
				PasswordUtils.clearArray(passwordToConfirm);
				
			}
			
			if(insertedUser) {
				
				log.info("Registrazione avvenuta con successo");
				
				response.sendRedirect("sign-in");
				
				return;
				
			}  else {
				
				log.info("Errore nella registrazione: le password non corrispondono");
				
				response.sendRedirect("sign-in");
				
				return;
				
			}
			
		} catch (SQLException | NoSuchAlgorithmException | ClassNotFoundException | ForbiddenFileTypeException | SAXException | TikaException e) {
			
			log.error("Errore in SignUpServlet: " + e.getMessage());
			
		} finally {
			
			photoInputStream.close();
			
		}
		
		doGet(request, response);
		
	}
	
}

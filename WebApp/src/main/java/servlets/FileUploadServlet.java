package servlets;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import dao.CookieDAO;
import dao.FileUploadDAO;
import enumeration.PropertiesKeys;
import exception.ForbiddenFileTypeException;
import utils.ApplicationPropertiesLoader;
import utils.EncryptionUtils;
import utils.FileUtils;
import utils.Utils;

/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet("/file-upload")
@MultipartConfig(
	    maxFileSize = 1024 * 1024 * 2,  // 5 MB
	    maxRequestSize = 1024 * 1024 * 10) // 10 MB
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
		boolean isUserAuthenticated = false;
		
		HttpSession currentSession = request.getSession(false);
		
		if(currentSession != null) {
			
			String user = (String) currentSession.getAttribute("user");
			
			if(user != null)
				isUserAuthenticated = true;
			
		}
		
		if(isUserAuthenticated) {
			
			request.getRequestDispatcher("file-upload.jsp").forward(request, response);
			return;
			
		}
		
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
	                		
	                		log.info(deletedCookie ? "Il cookie è stato cancellato correttamente dal database" : "Il cookie NON è stato cancellato correttamente dal database");
	                		
	                		response.sendRedirect("./index.jsp");
	                		return;
	                		
	                	}
	                	
	                }
	                
	            }
	            
	        }
	        
		} catch (Exception e) {

			log.error("Eccezione in FileUploadServlet: ", e);
			
		}
		
		request.getRequestDispatcher("file-upload.jsp").forward(request, response);
		
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
    	Cookie[] cookies = request.getCookies();
    	String cookieUsername = null;
    	String sessionUsername = null;
    	
    	if(cookies != null) {
    		
    		for(Cookie cookie : cookies) {
    			
    			if(cookie.getName().equals("rememberMe")) 
    				
    				cookieUsername = Utils.getUsernameFromCookie(cookie.getValue());

    			
    		}
    		
    	}
    	
    	sessionUsername = (String) request.getSession(false).getAttribute("user");
    	
    	if(cookieUsername == null && request.getSession(false) == null) {
    		
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
    		
    	}
    	
    	try {
			
            /*
             * Recupero il contenuto del file e lo scrivo sul database
             * 
             * */
            
            Part filePart = request.getPart("file");
            
            String fileName = getFileName(filePart);
            
//            String finalPath = prop.getProperty(PropertiesKeys.PATH_UPLOADED_FILES.toString()) + File.separator + fileName;
            String finalPath = getServletContext().getRealPath("/") + File.separator + Utils.transformFileName(fileName);
            
            System.out.println("REAL PATH = " + getServletContext().getRealPath("/") + File.separator + fileName);
            
            filePart.write(finalPath);
            
    		HttpSession currentSession = request.getSession(false);
    		
    		String finalUsername = cookieUsername != null ? cookieUsername : sessionUsername;
    		
    		if(FileUtils.isFileTypeForbidden(finalPath))
    			throw new ForbiddenFileTypeException(finalPath);
    		
            FileUploadDAO.saveFileToDatabase(getFileName(filePart), FileUtils.getFileContent(new File(finalPath)), finalUsername);
            
            currentSession.setAttribute("uploadedFileName", fileName);
            
		} catch (Exception e) {
			
			log.error(e.getMessage());
			
		} 
    	
        doGet(request, response);
        
    }
    
    private String getFileName(Part part) {
    	
        String contentDisposition = part.getHeader("content-disposition");
        
        String[] tokens = contentDisposition.split(";");
        
        for (String token : tokens) {
        	
            if (token.trim().startsWith("filename")) 
                return token.substring(token.indexOf('=') + 2, token.length() - 1);
            
        }
        
        return "";
    }

}

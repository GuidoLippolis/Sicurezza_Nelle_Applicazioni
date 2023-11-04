package servlets;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import constants.Constants;
import dao.CookieDAO;
import dao.FileUploadDAO;
import exception.FileTooLargeException;
import exception.ForbiddenFileTypeException;
import model.UploadedFile;
import net.jcip.annotations.ThreadSafe;
import utils.ApplicationPropertiesLoader;
import utils.FileUtils;
import utils.Utils;

/**
 * Servlet implementation class FileUploadServlet
 */
@MultipartConfig
@ThreadSafe
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
    
    @SuppressWarnings("unused")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	// Viene recuperato lo username dell'utente di un'eventuale sessione aperta
    	
    	HttpSession session = request.getSession(false);
    	
    	String sessionUser = null;
    	
    	if(session != null)
    		sessionUser = (String) request.getSession(false).getAttribute("user");
        
		boolean isRememberMeCookieExpired = true;
		boolean deletedRememberMeCookie = false;
		
		Cookie[] cookies = request.getCookies();

		try {
			
			if(cookies != null) {
				
				for(Cookie cookie : cookies) {
					
					if(cookie.getName().equals("rememberMe")) {
						
						isRememberMeCookieExpired = Utils.isCookieExpired(CookieDAO.findCookieExpirationTimeByUserId(Utils.getUserIdFromCookieValue(cookie.getValue())));

						if(isRememberMeCookieExpired) {
							
							deletedRememberMeCookie = CookieDAO.deleteCookieByValue(CookieDAO.findCookieByUserId(Utils.getUserIdFromCookieValue(cookie.getValue())));
							
							log.info(deletedRememberMeCookie ? "Il cookie è stato cancellato correttamente dal database" : "Il cookie è ancora nel database");
							
							request.getRequestDispatcher("index.jsp").forward(request, response);
							return;
							
						} else {
							
							log.info(deletedRememberMeCookie ? "Il cookie è stato cancellato correttamente dal database" : "Il cookie è ancora nel database");
							
							request.getSession().setAttribute("user", Utils.getUsernameFromCookie(cookie.getValue()));
							
						}
						
					}
					
				}
				
			}
			
		} catch (Exception e) {
			
			log.error("Eccezione in FileUploadServlet: " + e.getMessage());
			
		}
		
        if (sessionUser != null || !isRememberMeCookieExpired) {
        	
            List<UploadedFile> uploadedFiles = null;
            
			try {
				
				// Recupera i file dal database per tutti gli utenti per stamparli nella tabella in file-upload.jsp
				uploadedFiles = FileUploadDAO.findAll();
				
			} catch (ClassNotFoundException | SQLException e) {

				log.error("Eccezione in FileUploadServlet: " + e.getMessage());
				
			}

            request.setAttribute("uploadedFiles", uploadedFiles);
            
            request.getRequestDispatcher("file-upload.jsp").forward(request, response);
            
        } else 
        	
            response.sendRedirect("sign-in");
        
    }
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
    	Cookie[] cookies = request.getCookies();
    	String cookieUsername = null;
    	String sessionUsername = null;
    	boolean savedFile = false;
    	
    	if(cookies != null) {
    		
    		for(Cookie cookie : cookies) {
    			
    			if(cookie.getName().equals("rememberMe")) 
    				
    				cookieUsername = Utils.getUsernameFromCookie(cookie.getValue());

    			
    		}
    		
    	}
    	
    	// Viene recuperato lo username dell'utente per memorizzarlo nel database
    	sessionUsername = (String) request.getSession().getAttribute("user");
    	
    	try {
			
            /*
             * Recupero il contenuto del file e lo scrivo sul database
             * 
             * */
            
            Part filePart = request.getPart("file");
            
            String fileName = FileUtils.getFileName(filePart);
            
            /*
             * Il metodo transformFileName aggiunge un timestamp al nome del file per evitare duplicati. In questo
             * modo viene gestita la situazione in cui due utenti diversi caricano un file con lo stesso nome
             * 
             * */
            
            String finalPath = getServletContext().getRealPath("/") + File.separator + Utils.transformFileName(fileName);
            
            filePart.write(finalPath);
    		
    		String finalUsername = cookieUsername != null ? cookieUsername : sessionUsername;
    		
    		if(FileUtils.isFileTooLarge(finalPath))
    			throw new FileTooLargeException(fileName, Long.parseLong(prop.getProperty(Constants.MAX_FILE_SIZE_MB.toString())));
    		
    		if(FileUtils.isFakeTxt(finalPath))
    			throw new ForbiddenFileTypeException(fileName);
    		
            savedFile = FileUploadDAO.saveFileToDatabase(FileUtils.getFileName(filePart), FileUtils.getFileBytes(new File(finalPath)), finalUsername);
            
            log.info(savedFile ? "Il file " + fileName + " è stato salvato correttamente sul database" : "Il file " + fileName + " NON è stato salvato correttamente sul database");
            
		} catch (ForbiddenFileTypeException e) {
			
			request.setAttribute("errorMessage", e.getMessage());
			log.error("Eccezione in FileUploadServlet: " + e.getMessage());
			
		} catch (FileTooLargeException e) {
			
			request.setAttribute("errorMessage", e.getMessage());
			log.error("Eccezione in FileUploadServlet: " + e.getMessage());
			
		} catch (ClassNotFoundException | SQLException | SAXException | TikaException e) {
			
			log.error("Eccezione in FileUploadServlet: " + e.getMessage());
			
		} 
    	
        doGet(request, response);
        
    }
    
}

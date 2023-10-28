package servlets;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import dao.FileUploadDAO;
import exception.ForbiddenFileTypeException;
import model.UploadedFile;
import net.jcip.annotations.ThreadSafe;
import utils.FileUtils;
import utils.Utils;

/**
 * Servlet implementation class FileUploadServlet
 */
@MultipartConfig(
	    maxFileSize = 1024 * 1024 * 2) // 2 MB
@ThreadSafe
public class FileUploadServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(FileUploadServlet.class);
	
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

    	// Viene recuperato lo username dell'utente di un'eventuale sessione aperta
        String sessionUser = (String) request.getSession(false).getAttribute("user");
        boolean isRememberMePresent = false;

        
        Cookie[] cookies = request.getCookies();

        // Si cerca un eventuale Cookie "rememberMe"
        if (cookies != null) {
        	
            for (Cookie cookie : cookies) {
            	
                if (cookie.getName().equals("rememberMe")) {
                	
                    String rememberedUser = Utils.getUsernameFromCookie(cookie.getValue());
                    
                    sessionUser = rememberedUser;
                    
                    isRememberMePresent = true;
                    
                    break;
                    
                }
            }
        }

        if (sessionUser != null || isRememberMePresent) {
        	
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
    	
    	if(cookies != null) {
    		
    		for(Cookie cookie : cookies) {
    			
    			if(cookie.getName().equals("rememberMe")) 
    				
    				cookieUsername = Utils.getUsernameFromCookie(cookie.getValue());

    			
    		}
    		
    	}
    	
    	// Viene recuperato lo username dell'utente per memorizzarlo nel database
    	sessionUsername = (String) request.getSession(false).getAttribute("user");
    	
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
    		
    		if(FileUtils.isFakeTxt(finalPath)) {
    			
    			request.getSession(false).setAttribute("errorMessage", "ATTENZIONE! Tipo di file non consentito!");
    			throw new ForbiddenFileTypeException(finalPath);
    			
    		}
    		
            FileUploadDAO.saveFileToDatabase(FileUtils.getFileName(filePart), FileUtils.getFileContent(new File(finalPath)), finalUsername);
            
		} catch (Exception e) {
			
			log.error("Eccezione in FileUploadServlet: " + e.getMessage());
			
		} 
    	
        doGet(request, response);
        
    }
    
}

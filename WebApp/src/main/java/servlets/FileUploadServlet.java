package servlets;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
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
import model.UploadedFile;
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
        // Verifica se l'utente è loggato o ha un cookie "Remember Me" valido
        // Se non lo è, reindirizza alla pagina di accesso

        String sessionUser = (String) request.getSession().getAttribute("user");
        boolean isRememberMePresent = false;

        if (sessionUser == null) {
        	
            Cookie[] cookies = request.getCookies();

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
        }

        if (sessionUser != null || isRememberMePresent) {
        	
            // Recupera i file dal database per tutti gli utenti
        	
            List<UploadedFile> uploadedFiles = null;
            
			try {
				
				uploadedFiles = FileUploadDAO.getAllFilesForAllUsers();
				
			} catch (ClassNotFoundException e) {

				log.error(e.getMessage());
				
			} catch (SQLException e) {

				log.error(e.getMessage());
				
			}

            request.setAttribute("uploadedFiles", uploadedFiles);
            
            request.getRequestDispatcher("file-upload.jsp").forward(request, response);
            
        } else 
        	
            response.sendRedirect("./index.jsp");
        
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

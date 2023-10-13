package servlets;

import java.io.IOException;
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
import utils.ApplicationPropertiesLoader;
import utils.EncryptionUtils;
import utils.FileUtils;
import utils.Utils;

/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet("/file-upload")
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024, // 1 MB
	    maxFileSize = 1024 * 1024 * 5,  // 5 MB
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
	                		response.sendRedirect("./index.jsp");
	                		return;
	                		
	                	}
	                	
	                }
	                
	            }
	            
	        }
	        
		} catch (Exception e) {

			log.error("Eccezione in SignInServlet: ", e);
			
		}
		
		request.getRequestDispatcher("file-upload.jsp").forward(request, response);
		
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
    	Cookie[] cookies = request.getCookies();
    	int userId = 0;
    	
    	if(cookies != null) {
    		
    		for(Cookie cookie : cookies) {
    			
    			if(cookie.getName().equals("rememberMe"))
    				userId = Utils.extractUserId(cookie.getValue());
    			
    		}
    		
    	}
    	
    	if(userId == 0) {
    		
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
    		
    	}
    	
    	try {
			
            /*
             * Recupero il contenuto del file e lo scrivo sul database
             * 
             * */
            
            Part filePart = request.getPart("file");
            
            FileUploadDAO.saveFileToDatabase(getFileName(filePart), FileUtils.getFileContent(filePart), userId);
            
		} catch (Exception e) {

			log.error("Eccezione in FileUploadServlet: ", e);
		
		}
    	
        doGet(request, response);
        
    }
	
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//       
//    	String uploadDirectory = "C:\\Users\\Guido\\Universita\\Sicurezza_Nelle_Applicazioni\\webapp-project\\uploaded_files";
//    	
//    	Cookie[] cookies = request.getCookies();
//    	int userId = 0;
//    	int countFiles = 0;
//    	
//    	if(cookies != null) {
//    		
//    		for(Cookie cookie : cookies) {
//    			
//    			if(cookie.getName().equals("rememberMe"))
//    				userId = Utils.extractUserId(cookie.getValue());
//    			
//    		}
//    		
//    	}
//    	
//    	try {
//			
//            File uploadDir = new File(uploadDirectory);
//            
//            if (!uploadDir.exists())
//                uploadDir.mkdir();
//            
//            for (Part part : request.getParts()) {
//            	
//                if (part.getContentType() != null && part.getContentType().equals("text/plain")) {
//                	
//                    String fileName = getFileName(part);
//                    String filePath = uploadDirectory + File.separator + fileName;
//
//                    try (InputStream fileContent = part.getInputStream()) {
//                    	
//                    	for(File file : Arrays.asList(uploadDir.listFiles())) {
//                    		
//                    		countFiles++;
//                    		
//                    		if(file.exists()) {
//                    			
//                    			filePath.concat("_" + countFiles);
//                        		Files.copy(fileContent, Paths.get(filePath));
//                        		FileUploadDAO.saveFileToDatabase(fileName, filePath, userId);
//                    			
//                    		} else {
//                    			
//                        		Files.copy(fileContent, Paths.get(filePath));
//                        		FileUploadDAO.saveFileToDatabase(fileName, filePath, userId);
//                    			
//                    		}
//                    		
//                    	}
//                    	
////                    	if(!Files.exists(Paths.get(filePath))) {
////                    		
////                    		Files.copy(fileContent, Paths.get(filePath));
////                    		FileUploadDAO.saveFileToDatabase(fileName, filePath, userId);
////                    		
////                    	} else {
////                    		
////                    		Files.copy(fileContent, Paths.get(filePath));
////                    		FileUploadDAO.saveFileToDatabase(fileName, filePath, userId);
////                    		
////                    	}
//                        
//                    }
//                    
//                }
//            }
//    		
//		} catch (Exception e) {
//
//			log.error("Eccezione in FileUploadServlet: ", e);
//		
//		}
//    	
//        doGet(request, response);
//        
//    }
    
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

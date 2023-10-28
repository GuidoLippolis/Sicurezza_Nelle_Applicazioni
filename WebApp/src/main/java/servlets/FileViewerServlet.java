package servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import dao.FileUploadDAO;
import net.jcip.annotations.ThreadSafe;

/**
 * Servlet implementation class FileDownloadServlet
 */
@ThreadSafe
public class FileViewerServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(FileViewerServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileViewerServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	int id = Integer.parseInt(request.getParameter("id"));

        if (id != 0) {
        	
            try {
            	
                byte[] fileContent = FileUploadDAO.getFileContent(id);
                
                if (fileContent != null) {
                    
                	log.info("File trovato nel database");
                	
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");

                    OutputStream out = response.getOutputStream();
                    out.write(fileContent);
                    out.flush();
                    
                } else 
                	
                    response.getWriter().write("File not found");
                
            } catch (Exception e) {
                
                log.error("Eccezione in FileViewerServlet: " + e.getMessage());
                response.getWriter().write("Error occurred");
                
            }
            
        } else
        	
            response.getWriter().write("Invalid request");
        
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
		
	}

}
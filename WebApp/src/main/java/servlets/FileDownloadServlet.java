package servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.FileUploadDAO;

/**
 * Servlet implementation class FileDownloadServlet
 */
@WebServlet("/FileDownloadServlet")
public class FileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileDownloadServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String fileName = request.getParameter("fileName");

        if (username != null && fileName != null) {
            try {
                byte[] fileContent = FileUploadDAO.getFileContent(username, fileName);
                if (fileContent != null) {
                    // Set the content type for text files (text/plain)
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8"); // Set to the appropriate encoding if needed

                    // Send the file content to the response output stream
                    OutputStream out = response.getOutputStream();
                    out.write(fileContent);
                    out.flush();
                } else {
                    // Handle the case where the file does not exist.
                    response.getWriter().write("File not found");
                }
            } catch (Exception e) {
                // Handle exceptions
                e.printStackTrace();
                response.getWriter().write("Error occurred");
            }
        } else {
            // Handle the case where the parameters are missing.
            response.getWriter().write("Invalid request");
        }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}

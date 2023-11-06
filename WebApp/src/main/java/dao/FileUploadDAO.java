package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import constants.Constants;
import model.UploadedFile;

public class FileUploadDAO {
	
	private static final Logger log = Logger.getLogger(FileUploadDAO.class);
	
	public static boolean saveFileToDatabase(String fileName, byte[] fileContent, String username) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement uploaedFilesStatement = null;
		
		try {
			
			connection = DriverManager.getConnection(
					
					System.getenv(Constants.JDBC_URL) + System.getenv(Constants.USERS_DB_NAME), 
					System.getenv(Constants.USERS_DB_USERNAME), 
					System.getenv(Constants.USERS_DB_PASSWORD)
					
			);
			
			connection.setAutoCommit(false);
			
			uploaedFilesStatement = connection.prepareStatement("INSERT INTO users_db.uploaded_files(file_name, file_data, username) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
			
			uploaedFilesStatement.setString(1, fileName);
			uploaedFilesStatement.setBytes(2, fileContent);
			uploaedFilesStatement.setString(3, username);
			
			int rowsAffectedUploadedFiles = uploaedFilesStatement.executeUpdate();
			
			if(rowsAffectedUploadedFiles == 1)
				connection.commit();
			else
				connection.rollback();
			
			return true;
			
		} catch (Exception e) {

			log.error("Eccezione in FileUploadDAO: ", e);
			return false;
		
		} finally {
			
			if(connection != null)
				connection.close();
			
			if(uploaedFilesStatement != null)
				uploaedFilesStatement.close();
			
		}
		
	}
	
	public static List<UploadedFile> findAll() throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement uploaedFilesStatement = null;
		
		ResultSet resultSetUploadedFiles = null;
		
		List<UploadedFile> uploadedFilesList = new ArrayList<UploadedFile>();
		
		try {
			
			connection = DriverManager.getConnection(
					
					System.getenv(Constants.JDBC_URL) + System.getenv(Constants.USERS_DB_NAME), 
					System.getenv(Constants.USERS_DB_USERNAME), 
					System.getenv(Constants.USERS_DB_PASSWORD)
					
			);
			
			uploaedFilesStatement = connection.prepareStatement("SELECT * FROM users_db.uploaded_files");
			
			resultSetUploadedFiles = uploaedFilesStatement.executeQuery();
			
			while(resultSetUploadedFiles.next()) {
				
				uploadedFilesList.add(new UploadedFile(resultSetUploadedFiles.getInt("id"), resultSetUploadedFiles.getString("file_name"), resultSetUploadedFiles.getBytes("file_data"), resultSetUploadedFiles.getString("username")));
			
			}
			
			return uploadedFilesList;
			
		} catch (Exception e) {

			log.error("Eccezione in FileUploadDAO: ", e);
			return uploadedFilesList;
		
		} finally {
			
			if(connection != null)
				connection.close();
			
			if(uploaedFilesStatement != null)
				uploaedFilesStatement.close();
			
			if(resultSetUploadedFiles != null)
				resultSetUploadedFiles.close();
			
		}
		
	}
	
	public static byte[] getFileContent(int id) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement uploaedFilesStatement = null;
		
		ResultSet resultSetUploadedFiles = null;
		
		try {
			
			connection = DriverManager.getConnection(
					
					System.getenv(Constants.JDBC_URL) + System.getenv(Constants.USERS_DB_NAME), 
					System.getenv(Constants.USERS_DB_USERNAME), 
					System.getenv(Constants.USERS_DB_PASSWORD)
					
			);
			
			uploaedFilesStatement = connection.prepareStatement("SELECT * FROM users_db.uploaded_files WHERE id = ?");
			
			uploaedFilesStatement.setInt(1, id);
			
			resultSetUploadedFiles = uploaedFilesStatement.executeQuery();
			
			if(resultSetUploadedFiles.next())
				return resultSetUploadedFiles.getBytes("file_data");
			
		} catch (Exception e) {

			log.error("Eccezione in FileUploadDAO: " + e.getMessage());
			return new byte[0];
		
		} finally {
			
			if(connection != null)
				connection.close();
			
			if(uploaedFilesStatement != null)
				uploaedFilesStatement.close();
			
			if(resultSetUploadedFiles != null)
				resultSetUploadedFiles.close();
			
		}
		
		return new byte[0];
		
	}
	
}
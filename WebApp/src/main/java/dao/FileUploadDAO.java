package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import enumeration.PropertiesKeys;
import model.UploadedFile;
import utils.ApplicationPropertiesLoader;

public class FileUploadDAO {
	
	private static Properties prop = ApplicationPropertiesLoader.getProperties();
	
	private static final Logger log = Logger.getLogger(FileUploadDAO.class);

	public static boolean saveFileToDatabase(String fileName, byte[] fileContent, String username) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement uploaedFilesStatement = null;
		
		try {
			
			connection = DriverManager.getConnection(
					
					prop.getProperty(PropertiesKeys.JDCB_URL.toString()) + prop.getProperty(PropertiesKeys.USERS_DB_NAME.toString()), 
					prop.getProperty(PropertiesKeys.USERS_DB_USERNAME.toString()), 
					prop.getProperty(PropertiesKeys.USERS_DB_PASSWORD.toString())
					
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
	
	public static List<UploadedFile> getFilesForUser(String username) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement uploaedFilesStatement = null;
		
		ResultSet resultSetUploadedFiles = null;
		
		List<UploadedFile> uploadedFilesList = new ArrayList<UploadedFile>();
		
		try {
			
			connection = DriverManager.getConnection(
					
					prop.getProperty(PropertiesKeys.JDCB_URL.toString()) + prop.getProperty(PropertiesKeys.USERS_DB_NAME.toString()), 
					prop.getProperty(PropertiesKeys.USERS_DB_USERNAME.toString()), 
					prop.getProperty(PropertiesKeys.USERS_DB_PASSWORD.toString())
					
			);
			
			uploaedFilesStatement = connection.prepareStatement("SELECT * FROM users_db.uploaded_files WHERE username = ?");
			
			uploaedFilesStatement.setString(1, username);
			
			resultSetUploadedFiles = uploaedFilesStatement.executeQuery();
			
			while(resultSetUploadedFiles.next())
				uploadedFilesList.add(new UploadedFile(resultSetUploadedFiles.getString("file_name"), resultSetUploadedFiles.getBytes("file_data"), resultSetUploadedFiles.getString("username")));
			
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
	
	public static List<UploadedFile> getAllFilesForAllUsers() throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement uploaedFilesStatement = null;
		
		ResultSet resultSetUploadedFiles = null;
		
		List<UploadedFile> uploadedFilesList = new ArrayList<UploadedFile>();
		
		try {
			
			connection = DriverManager.getConnection(
					
					prop.getProperty(PropertiesKeys.JDCB_URL.toString()) + prop.getProperty(PropertiesKeys.USERS_DB_NAME.toString()), 
					prop.getProperty(PropertiesKeys.USERS_DB_USERNAME.toString()), 
					prop.getProperty(PropertiesKeys.USERS_DB_PASSWORD.toString())
					
			);
			
			uploaedFilesStatement = connection.prepareStatement("SELECT * FROM users_db.uploaded_files");
			
			resultSetUploadedFiles = uploaedFilesStatement.executeQuery();
			
			while(resultSetUploadedFiles.next())
				uploadedFilesList.add(new UploadedFile(resultSetUploadedFiles.getString("file_name"), resultSetUploadedFiles.getBytes("file_data"), resultSetUploadedFiles.getString("username")));
			
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
	
}
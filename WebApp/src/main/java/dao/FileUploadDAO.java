package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

import enumeration.PropertiesKeys;
import utils.ApplicationPropertiesLoader;

public class FileUploadDAO {
	
	private static Properties prop = ApplicationPropertiesLoader.getProperties();
	
	private static final Logger log = Logger.getLogger(FileUploadDAO.class);

	public static boolean saveFileToDatabase(String fileName, byte[] fileContent, int userId) throws ClassNotFoundException, SQLException {
		
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
			
			uploaedFilesStatement = connection.prepareStatement("INSERT INTO users_db.uploaded_files(file_name, file_data, user_id) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
			
			uploaedFilesStatement.setString(1, fileName);
			uploaedFilesStatement.setBytes(2, fileContent);
			uploaedFilesStatement.setInt(3, userId);
			
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
	
}
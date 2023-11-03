package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import constants.Constants;
import utils.ApplicationPropertiesLoader;

public class PasswordDAO {

	private static final Logger log = Logger.getLogger(PasswordDAO.class);
	
	private static Properties prop = ApplicationPropertiesLoader.getProperties();

	public static boolean findUserByPassword(byte[] hashedPassword) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement preparedStatement = null;
		
		ResultSet resultSet = null;
		
		try {
			
			connection = DriverManager.getConnection(
					
					System.getenv(Constants.JDBC_URL) + System.getenv(Constants.PASSWORDS_DB_NAME), 
					System.getenv(Constants.PASSWORDS_DB_USERNAME), 
					System.getenv(Constants.PASSWORDS_DB_PASSWORD)
					
			);
			
			preparedStatement = connection.prepareStatement("SELECT * FROM passwords WHERE password = ?");
			
			preparedStatement.setBytes(1, hashedPassword);
			
			resultSet = preparedStatement.executeQuery();
			
			boolean found = resultSet.next();
			
			if(found)
				return true;
			else
				return false;
			
		} catch (Exception e) {
			
			log.error("Eccezione in PasswordDAO: " + e.getMessage());
			return false;
			
		} finally {
			
			if(connection != null)
				connection.close();
			
			if(preparedStatement != null)
				preparedStatement.close();
			
			if(resultSet != null)
				resultSet.close();
			
		}
		
	}
	
}

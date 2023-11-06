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

public class SaltDAO {
	
	private static final Logger log = Logger.getLogger(SaltDAO.class);
	
	public static byte[] findSaltByUsername(String username) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;

		PreparedStatement preparedStatement = null;
		
		ResultSet resultSet = null;
		
		try {
			
			connection = DriverManager.getConnection(
					
					System.getenv(Constants.JDBC_URL) + System.getenv(Constants.SALTS_DB_NAME), 
					System.getenv(Constants.SALTS_DB_USERNAME), 
					System.getenv(Constants.SALTS_DB_PASSWORD)
					
			);
			
			preparedStatement = connection.prepareStatement("SELECT salt FROM salts WHERE username = ?");
			
			preparedStatement.setString(1, username);
			
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next())
				return resultSet.getBytes("salt");
			
		} catch (Exception e) {

			log.error("Eccezione in SaltDAO: " + e.getMessage());
			return new byte[0];
			
		} finally {
			
			if(connection != null)
				connection.close();
			
			if(preparedStatement != null)
				preparedStatement.close();
			
			if(resultSet != null)
				resultSet.close();
			
		}
		
		return new byte[0];
		
	}

}

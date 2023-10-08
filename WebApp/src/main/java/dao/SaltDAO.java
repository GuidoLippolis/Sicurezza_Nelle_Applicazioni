package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import enumeration.PropertiesKeys;
import utils.ApplicationPropertiesLoader;

public class SaltDAO {
	
	private static Properties prop = ApplicationPropertiesLoader.getProperties();

	public static byte[] findSaltByUsername(String username) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;

		PreparedStatement preparedStatement = null;
		
		ResultSet resultSet = null;
		
		try {
			
			connection = DriverManager.getConnection(
					
					prop.getProperty(PropertiesKeys.JDCB_URL.toString()) + prop.getProperty(PropertiesKeys.SALTS_DB_NAME.toString()), 
					prop.getProperty(PropertiesKeys.SALTS_DB_USERNAME.toString()), 
					prop.getProperty(PropertiesKeys.SALTS_DB_PASSWORD.toString())
					
			);
			
			preparedStatement = connection.prepareStatement("SELECT salt FROM salts WHERE username = ?");
			
			preparedStatement.setString(1, username);
			
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next())
				return resultSet.getBytes("salt");
			
		} catch (Exception e) {

			e.printStackTrace();
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

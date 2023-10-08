package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import enumeration.PropertiesKeys;
import utils.ApplicationPropertiesLoader;

public class PasswordDAO {

	private static Properties prop = ApplicationPropertiesLoader.getProperties();

	public static boolean findUserByPassword(byte[] hashedPassword) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement preparedStatement = null;
		
		ResultSet resultSet = null;
		
		try {

			connection = DriverManager.getConnection(
					
					prop.getProperty(PropertiesKeys.JDCB_URL.toString()) + prop.getProperty(PropertiesKeys.PASSWORDS_DB_NAME.toString()), 
					prop.getProperty(PropertiesKeys.PASSWORDS_DB_USERNAME.toString()), 
					prop.getProperty(PropertiesKeys.PASSWORDS_DB_PASSWORD.toString())
					
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
			
			e.printStackTrace();
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

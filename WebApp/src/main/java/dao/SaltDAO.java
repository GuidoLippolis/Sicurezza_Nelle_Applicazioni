package dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import enumeration.PropertiesKeys;

public class SaltDAO {
	
	private static Properties prop;

	static {
		
        prop = new Properties();
        
        try {
			
            prop = new Properties();
            
            FileInputStream in = new FileInputStream(System.getenv(PropertiesKeys.PATH_TO_PROPERTIES_FILE.toString()));
            
            prop.load(in);

            in.close();

		} catch (Exception e) {
			
			e.printStackTrace();
			
		}

	}
	
	public static byte[] findSaltByUsername(String username) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;

		PreparedStatement preparedStatement = null;
		
		ResultSet resultSet = null;
		
		try {
			
			System.out.println("URL = " + prop.getProperty(PropertiesKeys.JDCB_URL.toString()) + prop.getProperty(PropertiesKeys.SALTS_DB_NAME.toString()));
			System.out.println("USERNAME = " + prop.getProperty(PropertiesKeys.SALTS_DB_USERNAME.toString()));
			System.out.println("PASSWORD = " + prop.getProperty(PropertiesKeys.SALTS_DB_PASSWORD.toString()));
			
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

package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordDAO {

	public static boolean findUserByPassword(byte[] hashedPassword) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement preparedStatement = null;
		
		ResultSet resultSet = null;
		
		try {
			
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/passwords_db", "root", "WgAb_9114_2359");
			
			preparedStatement = connection.prepareStatement("SELECT * FROM passwords WHERE password = '" + hashedPassword + "'");
			
//			preparedStatement.setBytes(1, hashedPassword);
			
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
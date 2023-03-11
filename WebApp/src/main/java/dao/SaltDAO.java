package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SaltDAO {
	
	public static byte[] findSaltByUserEmail(String userEmail) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;

		PreparedStatement preparedStatement = null;
		
		ResultSet resultSet = null;
		
		try {
			
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/salts_db", "root", "WgAb_9114_2359");
			
			preparedStatement = connection.prepareStatement("SELECT salt FROM salt_user WHERE user_email = '" + userEmail + "'");
			
//			preparedStatement.setString(1, userEmail);
			
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
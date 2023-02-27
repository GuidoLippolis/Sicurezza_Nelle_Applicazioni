package dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.LoginDao;

public class JDBCLoginDao implements LoginDao {

	private Connection connection;
	
	public JDBCLoginDao(Connection connection) {
		
		super();
		this.connection = connection;
		
	}

	@Override
	public boolean isUserValid(String email, byte[] password) throws SQLException {
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		boolean userValid = false;
		
		try {
			
			preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = " + email + " AND password = " + password);
			
			resultSet = preparedStatement.executeQuery();
			
			userValid = resultSet.next();

		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
			
		} finally {
			
			if(resultSet != null)
				resultSet.close();
			
			if(preparedStatement != null)
				preparedStatement.close();
			
		}
		
		return userValid;
		
	}

}
package dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dao.LoginDao;
import passwordUtils.PasswordUtils;

public class JDBCLoginDao implements LoginDao {

	private Connection connection;
	
	public JDBCLoginDao(Connection connection) {
		
		super();
		this.connection = connection;
		
	}
	
	@Override
	public boolean signUp(String email, String password, byte[] photo) throws SQLException {
		
		/*
		 * preparedStatementUsers serve per l'inserimento dell'utente nel database
		 * 
		 * */
		
		PreparedStatement preparedStatementUsers = null;
		int rowsAffected = 0;
		int generatedId = 0;
		
		/*
		 * preparedStatementPasswords serve per l'inserimento del valore hash
		 * della password nel database
		 * 
		 * */
		
		PreparedStatement preparedStatementPasswords = null;
		
		if(email.length() != 0) {
			
			try {
				
				/*
				 * Query per l'inserimento dell'utente nel database
				 * 
				 * */
				
				preparedStatementUsers = connection.prepareStatement("INSERT INTO users(email, photo) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
				
				preparedStatementUsers.setString(1, email);
				preparedStatementUsers.setBytes(2, photo);
				
				rowsAffected = preparedStatementUsers.executeUpdate();
				
				if(rowsAffected > 0) {
					
					ResultSet generatedKeys = preparedStatementUsers.getGeneratedKeys();
					
				    if (generatedKeys.next()) {
				    	
				    	byte[] hashedPassword = PasswordUtils.generateHash(password, "SHA-256", PasswordUtils.createSalt(5));
				    	
				    	generatedId = generatedKeys.getInt(1);
				    	
				    	preparedStatementPasswords = connection.prepareStatement("INSERT INTO passwords(user_id, password) VALUES(?,?)");
				    	
				    	preparedStatementPasswords.setInt(1, generatedId);
				    	preparedStatementPasswords.setBytes(2, hashedPassword);
				    	
				    	preparedStatementPasswords.executeUpdate();
				    	
				    } else
				    	
				    	return false;
					
				}
				
			} catch (Exception e) {
				
				e.printStackTrace();
				return false;
				
			} finally {
				
				if(preparedStatementUsers != null)
					preparedStatementUsers.close();
				
			}
			
		} else
			
			return false;
			
		return true;
		
	}

	@Override
	public boolean isUserValid(String email, byte[] password) throws SQLException {
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		boolean userValid = false;
		
		try {
			
			preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?");
			
			preparedStatement.setString(1, email);
			preparedStatement.setBytes(2, password);
			
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
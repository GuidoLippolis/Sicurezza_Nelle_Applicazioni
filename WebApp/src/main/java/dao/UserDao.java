package dao;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import model.User;
import passwordUtils.PasswordUtils;

public class UserDAO {

	public static boolean signIn(User user, byte[] password) throws SQLException, NoSuchAlgorithmException, ClassNotFoundException {
		
        Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection passwordsDBConnection = null;
		Connection usersDBConnection = null;
		
		PreparedStatement preparedStatementUsers = null;
		PreparedStatement preparedStatementPasswords = null;
		
		byte[] hashedPassword = PasswordUtils.generateHash(password, "SHA-256");
		
		try {
			
			passwordsDBConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/passwords_db", "root", "WgAb_9114_2359");
			usersDBConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users_db", "root", "WgAb_9114_2359");
		
			passwordsDBConnection.setAutoCommit(false);
			
			preparedStatementUsers = usersDBConnection.prepareStatement("INSERT INTO users(email, photo) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
			
			preparedStatementUsers.setString(1, user.getEmail());
			preparedStatementUsers.setBytes(2, user.getPhoto());
			
			preparedStatementUsers.executeUpdate();
			
			ResultSet generatedKeys = preparedStatementUsers.getGeneratedKeys();
			
			int userId = 0;

			if(generatedKeys.next()) {
				
				userId = generatedKeys.getInt(1);
				
				preparedStatementPasswords = passwordsDBConnection.prepareStatement("INSERT INTO passwords(user_id, password) VALUES(?,?)");
				
				preparedStatementPasswords.setInt(1, userId);
				preparedStatementPasswords.setBytes(2, hashedPassword);
				
				preparedStatementPasswords.executeUpdate();
				
				passwordsDBConnection.commit();
				
				return true;
				
			}
			
		} catch (SQLException e) {
			
			if(passwordsDBConnection != null)
				passwordsDBConnection.rollback();
			
			e.printStackTrace();
			
			return true;
			
		} finally {
			
			if(usersDBConnection != null)
				usersDBConnection.close();
			
			if(passwordsDBConnection != null)
				passwordsDBConnection.close();
			
			if(preparedStatementUsers != null)
				preparedStatementUsers.close();
			
			if(preparedStatementPasswords != null)
				preparedStatementPasswords.close();
			
		}
	
		return true;
		
	}
}

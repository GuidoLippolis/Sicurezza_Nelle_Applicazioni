package dao;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

import javax.naming.InitialContext;

import model.User;
import passwordUtils.PasswordUtils;

public class UserDAO {
	
	public static boolean signUp(final User user, byte[] password) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection usersConnection = null;
		Connection passwordsConnection = null;
		
		PreparedStatement usersStatement = null;
		PreparedStatement passwordsStatement = null;
		
		ResultSet resultSetUsers = null;
		
		int userId = 0;
		
		/*
		 * TODO: Il parametro di createSalt va recuperato da un file .properties
		 * TODO: Le query e l'algoritmo di hashing vanno anche recuperate da un file .properties
		 * TODO: Aggiungere query INSERT sul database salts_db
		 * 
		 * */
		
		byte[] salt = PasswordUtils.createSalt(10);
		byte[] hashedPassword = PasswordUtils.generateHash(password, salt, "SHA-256");
		
		try {

			usersConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users_db", "root", "WgAb_9114_2359");
			passwordsConnection= DriverManager.getConnection("jdbc:mysql://localhost:3306/passwords_db", "root", "WgAb_9114_2359");
			
			usersConnection.setAutoCommit(false);
			passwordsConnection.setAutoCommit(false);
			
			usersStatement = usersConnection.prepareStatement("INSERT INTO users(email, photo) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
			
			usersStatement.setString(1, user.getEmail());
			usersStatement.setBytes(2, user.getPhoto());
			
			int rowsAffectedUsers = usersStatement.executeUpdate();
			
			if(rowsAffectedUsers > 0) {
				
				resultSetUsers = usersStatement.getGeneratedKeys();
				resultSetUsers.next();
				
				userId = resultSetUsers.getInt(1);
				
				usersConnection.commit();
				
			}
			
			passwordsStatement = passwordsConnection.prepareStatement("INSERT INTO passwords_db.passwords(user_id, password) VALUES(?,?)");

			passwordsStatement.setInt(1, userId);
			passwordsStatement.setBytes(2, hashedPassword);
			
			passwordsStatement.executeUpdate();
			
			passwordsConnection.commit();
			
			return true;
			
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		
		} finally {
			
			if(usersConnection != null)
				usersConnection.close();
			
			if(usersConnection != null)
				usersConnection.close();
			
			if(usersStatement != null)
				usersStatement.close();
			
			if(passwordsStatement != null)
				passwordsStatement.close();
			
			if(resultSetUsers != null)
				resultSetUsers.close();
			
		}
		
		
	}
	
//	public static boolean signUp(final User user, byte[] password) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
//		
//		Class.forName("com.mysql.cj.jdbc.Driver");
//		
//		Connection connection = null;
//		
//		PreparedStatement usersStatement = null;
//		PreparedStatement passwordsStatement = null;
//		
//		ResultSet resultSetUsers = null;
//		
//		int userId = 0;
//		
//		/*
//		 * TODO: Il parametro di createSalt va recuperato da un file .properties
//		 * TODO: Le query e l'algoritmo di hashing vanno anche recuperate da un file .properties
//		 * TODO: Aggiungere query INSERT sul database salts_db
//		 * 
//		 * */
//		
//		byte[] salt = PasswordUtils.createSalt(10);
//		byte[] hashedPassword = PasswordUtils.generateHash(password, salt, "SHA-256");
//		
//		try {
//			
//			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users_db", "root", "WgAb_9114_2359");
//			
//			connection.setAutoCommit(false);
//			
//			usersStatement = connection.prepareStatement("INSERT INTO users(email, photo) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
//			
//			usersStatement.setString(1, user.getEmail());
//			usersStatement.setBytes(2, user.getPhoto());
//			
//			int rowsAffectedUsers = usersStatement.executeUpdate();
//			
//			if(rowsAffectedUsers > 0) {
//				
//				resultSetUsers = usersStatement.getGeneratedKeys();
//				resultSetUsers.next();
//				
//				userId = resultSetUsers.getInt(1);
//				
//			}
//			
//			passwordsStatement = connection.prepareStatement("INSERT INTO passwords_db.passwords(user_id, password) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
//
//			passwordsStatement.setInt(1, userId);
//			passwordsStatement.setBytes(2, hashedPassword);
//			
//			int rowsAffectedPasswords = passwordsStatement.executeUpdate();
//			
//			if(rowsAffectedUsers == 1 && rowsAffectedPasswords == 1) {
//				
//				connection.commit();
//				return true;
//				
//			} else {
//				
//				connection.rollback();
//				return false;
//				
//			}
//			
//		} catch (Exception e) {
//
//			e.printStackTrace();
//			
//			if(connection != null)
//				connection.rollback();
//			
//			return false;
//		
//		} finally {
//			
//			if(connection != null)
//				connection.close();
//			
//			if(usersStatement != null)
//				usersStatement.close();
//			
//			if(passwordsStatement != null)
//				passwordsStatement.close();
//			
//			if(resultSetUsers != null)
//				resultSetUsers.close();
//			
//		}
//		
//	}
	
}
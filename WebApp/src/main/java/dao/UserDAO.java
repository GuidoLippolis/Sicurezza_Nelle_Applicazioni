package dao;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.User;

public class UserDAO {
	
	public static boolean signUp(final User user, byte[] password) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection usersConnection = null;
		
		PreparedStatement usersStatement = null;
		PreparedStatement passwordsStatement = null;
		
		ResultSet resultSetUsers = null;
		
		int userId = 0;
		
		try {
			
			usersConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users_db", "root", "WgAb_9114_2359");
			
			usersConnection.setAutoCommit(false);
			
			usersStatement = usersConnection.prepareStatement("INSERT INTO users(email, photo) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
			
			usersStatement.setString(1, user.getEmail());
			usersStatement.setBytes(2, user.getPhoto());
			
			int rowsAffectedUsers = usersStatement.executeUpdate();
			
			if(rowsAffectedUsers > 0) {
				
				resultSetUsers = usersStatement.getGeneratedKeys();
				resultSetUsers.next();
				
				userId = resultSetUsers.getInt(1);
				
			}
			
			passwordsStatement = usersConnection.prepareStatement("INSERT INTO passwords_db.password(user_id, password) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);

			passwordsStatement.setInt(1, userId);
			passwordsStatement.setBytes(2, password);
			
			int rowsAffectedPasswords = passwordsStatement.executeUpdate();
			
			if(rowsAffectedUsers == 1 && rowsAffectedPasswords == 1) {
				
				usersConnection.commit();
				return true;
				
			} else {
				
				usersConnection.rollback();
				return false;
				
			}
			
		} catch (Exception e) {

			e.printStackTrace();
			
			if(usersConnection != null)
				usersConnection.rollback();
			
			return false;
		
		} finally {
			
			
			
		}
		
	}
	
//	public static boolean signUp(final User user, byte[] password) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
//		
//		Class.forName("com.mysql.cj.jdbc.Driver");
//
//		Connection usersConnection = null;
//		Connection passwordsConnection = null;
//		
//		PreparedStatement usersStatement = null;
//		PreparedStatement passwordsStatement = null;
//		
//		ResultSet userKeys = null;
//		
//		try {
//			
//			usersConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users_db", "root", "WgAb_9114_2359");
//			passwordsConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/passwords_db", "root", "WgAb_9114_2359");
//			
//			usersConnection.setAutoCommit(false);
//			passwordsConnection.setAutoCommit(false);
//			
//			usersStatement = usersConnection.prepareStatement("INSERT INTO users(email, photo) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
//			
//			usersStatement.setString(1, user.getEmail());
//			usersStatement.setBytes(2, user.getPhoto());
//			
//			int affectedRowsUsers = usersStatement.executeUpdate();
//			
//			if(affectedRowsUsers == 0) {
//				
//				passwordsConnection.rollback();
//				usersConnection.rollback();
//				return false;
//				
//			}
//			
//			userKeys = usersStatement.getGeneratedKeys();
//			
//			userKeys.next();
//			
//			int userId = userKeys.getInt(1);
//			
//			passwordsStatement = passwordsConnection.prepareStatement("INSERT INTO passwords(user_id, password) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
//			
//			passwordsStatement.setInt(1, userId);
//			passwordsStatement.setBytes(2, password);
//			
//			int affectedRowsPasswords = passwordsStatement.executeUpdate();
//			
//			if(affectedRowsPasswords == 0) {
//				
//				passwordsConnection.rollback();
//				usersConnection.rollback();
//				return false;
//
//			}
//			
//			usersConnection.commit();
//			passwordsConnection.commit();
//			
//			return true;
//			
//		} catch (Exception e) {
//
//			e.printStackTrace();
//			
//			if(usersConnection != null)
//				usersConnection.rollback();
//				
//			if(passwordsConnection != null)
//				passwordsConnection.rollback();
//			
//			return false;
//			
//		} finally {
//			
//			if(usersConnection != null)
//				usersConnection.close();
//			
//			if(passwordsConnection != null)
//				passwordsConnection.close();
//			
//			if(usersStatement != null)
//				usersStatement.close();
//			
//			if(passwordsStatement != null)
//				passwordsStatement.close();
//			
//			if(userKeys != null)
//				userKeys.close();
//			
//		}
//		
//	}
}
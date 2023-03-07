package dao;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;

import model.User;
import passwordUtils.PasswordUtils;

public class UserDAO {
	
	public static boolean signUp(final User user, byte[] password) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, InterruptedException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		final CountDownLatch latch = new CountDownLatch(1);

		byte[] salt = PasswordUtils.createSalt(10);
		final byte[] hashedPassword = PasswordUtils.generateHash(password, salt, "SHA-256");
		
		Thread usersThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				System.out.println("Users thread");

				Connection usersConnection = null;
				PreparedStatement usersStatement = null;
				ResultSet usersResultSet = null;
				
				try {
					
					usersConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users_db", "root", "WgAb_9114_2359");
					
					usersConnection.setAutoCommit(false);
					
					usersStatement = usersConnection.prepareStatement("INSERT INTO users(email,photo) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
					
					usersStatement.setString(1, user.getEmail());
					usersStatement.setBytes(2, user.getPhoto());
					
					usersStatement.executeUpdate();
					
					usersResultSet = usersStatement.getGeneratedKeys();
					
					if(usersResultSet.next()) {

						usersConnection.commit();
						
					}
					
				} catch (Exception e) {

					try {
						
						if(usersConnection != null)
							usersConnection.rollback();
						
					} catch (SQLException e1) {

						e1.printStackTrace();
						
					} finally {
						
						e.printStackTrace();
						
					}
					
				
				} finally {
					
					try {
						
						if(usersConnection != null)
							usersConnection.close();
						
						if(usersStatement != null)
							usersStatement.close();
						
						if(usersResultSet != null)
							usersResultSet.close();
						
					} catch (SQLException e) {

						e.printStackTrace();
					
					}
					
					
					
				}
				
				latch.countDown();
				
			}
			
		});
		
		Thread passwordsThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				System.out.println("Passwords thread");

				Connection passwordsConnection = null;
				PreparedStatement passwordsStatement = null;

				try {
					
					latch.await();
					
					passwordsConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/passwords_db", "root", "WgAb_9114_2359");

					passwordsConnection.setAutoCommit(false);
					
					passwordsStatement = passwordsConnection.prepareStatement("INSERT INTO passwords(user_email,password) VALUES(?,?)");
					
					passwordsStatement.setString(1, user.getEmail());
					passwordsStatement.setBytes(2, hashedPassword);
					
					passwordsStatement.executeUpdate();
					
					passwordsConnection.commit();
					
				} catch (Exception e) {

					try {
						
						if(passwordsConnection != null) {
							
							passwordsConnection.setAutoCommit(false);
							passwordsConnection.rollback();
							
						}
						
					} catch (SQLException e1) {
						
						e1.printStackTrace();
						
					} finally {
						
						e.printStackTrace();
						
					}
					
				} finally {
					
					try {
						
						if(passwordsConnection != null)
							passwordsConnection.close();
						
						if(passwordsStatement != null)
							passwordsStatement.close();
						
					} catch (SQLException e) {
						
						e.printStackTrace();
						
					}
					
				}
				
			}
			
		});
		
		usersThread.start();
		passwordsThread.start();
		
		usersThread.join();
		passwordsThread.join();
		
		return true;
		
	}
	
//	public static boolean signUp(final User user, byte[] password) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
//		
//		Class.forName("com.mysql.cj.jdbc.Driver");
//		
//		Connection usersConnection = null;
//		
//		PreparedStatement usersStatement = null;
//		PreparedStatement passwordsStatement = null;
//		
//		ResultSet resultSetUsers = null;
//		
//		int userId = 0;
//		
//		byte[] salt = PasswordUtils.createSalt(10);
//		byte[] hashedPassword = PasswordUtils.generateHash(password, salt, "SHA-256");
//		
//		try {
//			
//			usersConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users_db", "root", "WgAb_9114_2359");
//			
//			usersConnection.setAutoCommit(false);
//			
//			usersStatement = usersConnection.prepareStatement("INSERT INTO users(email, photo) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
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
//			passwordsStatement = usersConnection.prepareStatement("INSERT INTO passwords_db.passwords(user_id, password) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
//
//			passwordsStatement.setInt(1, userId);
//			passwordsStatement.setBytes(2, hashedPassword);
//			
//			int rowsAffectedPasswords = passwordsStatement.executeUpdate();
//			
//			if(rowsAffectedUsers == 1 && rowsAffectedPasswords == 1) {
//				
//				usersConnection.commit();
//				return true;
//				
//			} else {
//				
//				usersConnection.rollback();
//				return false;
//				
//			}
//			
//		} catch (Exception e) {
//
//			e.printStackTrace();
//			
//			if(usersConnection != null)
//				usersConnection.rollback();
//			
//			return false;
//		
//		} finally {
//			
//			if(usersConnection != null)
//				usersConnection.close();
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
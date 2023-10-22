package dao;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import enumeration.PropertiesKeys;
import model.User;
import utils.ApplicationPropertiesLoader;
import utils.PasswordUtils;

public class UserDAO {
	
	private static final Logger log = Logger.getLogger(UserDAO.class);
	
	private static Properties prop = ApplicationPropertiesLoader.getProperties();

	public static boolean signUp(User user, byte[] password) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement usersStatement = null;
		PreparedStatement passwordsStatement = null;
		PreparedStatement saltsStatement = null;
		
		ResultSet resultSetUsers = null;
		
		int userId = 0;
		
		/*
		 * TODO: Il parametro di createSalt va recuperato da un file .properties
		 * TODO: Le query e l'algoritmo di hashing vanno anche recuperate da un file .properties
		 * 
		 * */
		
		byte[] salt = PasswordUtils.createSalt(10);
		byte[] hashedPassword = PasswordUtils.generateHash(password, salt, prop.getProperty(PropertiesKeys.HASHING_ALGORITHM.toString()));
		
		try {
			
			connection = DriverManager.getConnection(
					
					prop.getProperty(PropertiesKeys.JDCB_URL.toString()) + prop.getProperty(PropertiesKeys.USERS_DB_NAME.toString()), 
					prop.getProperty(PropertiesKeys.USERS_DB_USERNAME.toString()), 
					prop.getProperty(PropertiesKeys.USERS_DB_PASSWORD.toString())
					
			);
			
			connection.setAutoCommit(false);
			
			usersStatement = connection.prepareStatement("INSERT INTO users_db.users(username, photo) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
			
			usersStatement.setString(1, user.getUsername());
			usersStatement.setBytes(2, user.getPhoto());
			
			int rowsAffectedUsers = usersStatement.executeUpdate();
			
			if(rowsAffectedUsers == 1) {
				
				resultSetUsers = usersStatement.getGeneratedKeys();
				resultSetUsers.next();
				
				userId = resultSetUsers.getInt(1);
				
				passwordsStatement = connection.prepareStatement("INSERT INTO passwords_db.passwords(user_id, password) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);

				passwordsStatement.setInt(1, userId);
				passwordsStatement.setBytes(2, hashedPassword);
				
				int rowsAffectedPasswords = passwordsStatement.executeUpdate();
				
				saltsStatement = connection.prepareStatement("INSERT INTO salts_db.salts(username,salt) VALUES(?,?)");
				
				saltsStatement.setString(1, user.getUsername());
				saltsStatement.setBytes(2, salt);
				
				int affectedRowsSalts = saltsStatement.executeUpdate();
				
				if(rowsAffectedPasswords == 1 && affectedRowsSalts == 1) {
					
					connection.commit();
					return true;
					
				} else {
					
					connection.rollback();
					return false;
					
				}
				
			}
			
		} catch (Exception e) {

			log.error("Eccezione in UserDAO: " + e.getMessage());
			
			if(connection != null)
				connection.rollback();
			
			return false;
		
		} finally {
			
			PasswordUtils.clearArray(salt);
			
			if(connection != null)
				connection.close();
			
			if(usersStatement != null)
				usersStatement.close();
			
			if(passwordsStatement != null)
				passwordsStatement.close();
			
			if(resultSetUsers != null)
				resultSetUsers.close();
			
		}
		
		return true;
		
	}
	
	public static boolean signIn(User user, byte[] password) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection passwordsConnection = null;
		
		PreparedStatement passwordsStatement = null;
		
		ResultSet resultSetPasswords = null;
		
		boolean loggedUser = false;
		
		try {
			
			/*
			 * Per autenticare l'utente vengono eseguiti i seguenti step:
			 * 
			 * - Recupero del valore del salt associato all'utente
			 * - Generazione del valore hash della concatenazione di password e salt
			 * 
			 * */
			
			byte[] userSalt = SaltDAO.findSaltByUsername(user.getUsername());
			
			byte[] hashedPasswordAndSalt = PasswordUtils.generateHash(password, userSalt, prop.getProperty(PropertiesKeys.HASHING_ALGORITHM.toString()));
			
			loggedUser = PasswordDAO.findUserByPassword(hashedPasswordAndSalt) ? true : false;
			
		} catch (Exception e) {

			log.error("Eccezione in UserDAO: " + e.getMessage());
			return false;
		
		} finally {
			
			if(passwordsConnection != null)
				passwordsConnection.close();
			
			if(passwordsStatement != null)
				passwordsStatement.close();
			
			if(resultSetPasswords != null)
				resultSetPasswords.close();
			
		}
		
		return loggedUser;
		
	}
	
	public static User findByUsername(String username) throws SQLException, ClassNotFoundException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement preparedStatement = null;
		
		ResultSet resultSet = null;
		
		List<User> users = new ArrayList<>();
		
		try {
			
			connection = DriverManager.getConnection(
					
					prop.getProperty(PropertiesKeys.JDCB_URL.toString()) + prop.getProperty(PropertiesKeys.USERS_DB_NAME.toString()), 
					prop.getProperty(PropertiesKeys.USERS_DB_USERNAME.toString()), 
					prop.getProperty(PropertiesKeys.USERS_DB_PASSWORD.toString())
					
			);
			
			preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
			
			preparedStatement.setString(1, username);
			
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next())
				users.add(new User(resultSet.getInt("id"), resultSet.getString("username")));
			
			return users.get(0);
			
		} catch (Exception e) {

			log.error("Eccezione in UserDAO: " + e.getMessage());
			return new User();
		
		} finally {
			
			if(connection != null)
				connection.close();
			
			if(resultSet != null)
				resultSet.close();
			
		}
		
	}
	
}
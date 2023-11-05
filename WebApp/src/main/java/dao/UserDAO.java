package dao;

import java.io.InputStream;
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

import constants.Constants;
import exception.UserAlreadyExistsException;
import model.User;
import utils.ApplicationPropertiesLoader;
import utils.PasswordUtils;

public class UserDAO {
	
	private static final Logger log = Logger.getLogger(UserDAO.class);
	
	private static Properties prop = ApplicationPropertiesLoader.getProperties();

	public static boolean signUp(String username, InputStream photo, byte[] password) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, UserAlreadyExistsException {

		User user = UserDAO.findByUsername(username);
		
		if(user.getUsername() != null)
			throw new UserAlreadyExistsException(username);
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement usersStatement = null;
		PreparedStatement passwordsStatement = null;
		PreparedStatement saltsStatement = null;
		
		ResultSet resultSetUsers = null;
		
		int userId = 0;
		
		byte[] salt = PasswordUtils.createSalt(10);
		byte[] hashedPassword = PasswordUtils.generateHash(password, salt, prop.getProperty(Constants.HASHING_ALGORITHM));
		
		try {
			
			connection = DriverManager.getConnection(
					
					System.getenv(Constants.JDBC_URL) + System.getenv(Constants.USERS_DB_NAME), 
					System.getenv(Constants.USERS_DB_USERNAME), 
					System.getenv(Constants.USERS_DB_PASSWORD)
					
			);
			
			connection.setAutoCommit(false);
			
			usersStatement = connection.prepareStatement("INSERT INTO users_db.users(username, photo) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
			
			usersStatement.setString(1, username);

			if(photo != null)
				usersStatement.setBlob(2, photo);
			
			usersStatement.executeUpdate();
			
			resultSetUsers = usersStatement.getGeneratedKeys();
			resultSetUsers.next();
			
			userId = resultSetUsers.getInt(1);
			
			passwordsStatement = connection.prepareStatement("INSERT INTO passwords_db.passwords(user_id, password) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);

			passwordsStatement.setInt(1, userId);
			passwordsStatement.setBytes(2, hashedPassword);
			
			passwordsStatement.executeUpdate();
			
			saltsStatement = connection.prepareStatement("INSERT INTO salts_db.salts(username,salt) VALUES(?,?)");
			
			saltsStatement.setString(1, username);
			saltsStatement.setBytes(2, salt);
			
			saltsStatement.executeUpdate();
			
			connection.commit();
			
			return true;
				
		} catch (Exception e) {

			log.error("Eccezione in UserDAO: " + e.getMessage());
			
			if(connection != null)
				connection.rollback();
			
			return false;
		
		} finally {
			
			PasswordUtils.clearArray(password);
			PasswordUtils.clearArray(salt);
			PasswordUtils.clearArray(hashedPassword);
			
			if(connection != null)
				connection.close();
			
			if(usersStatement != null)
				usersStatement.close();
			
			if(passwordsStatement != null)
				passwordsStatement.close();
			
			if(resultSetUsers != null)
				resultSetUsers.close();
			
		}
		
//		return true;
		
	}
	
	public static boolean signIn(String username, byte[] password) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection passwordsConnection = null;
		
		PreparedStatement passwordsStatement = null;
		
		ResultSet resultSetPasswords = null;
		
		boolean loggedUser = false;
		
		byte[] userSalt = new byte[0];
		byte[] hashedPasswordAndSalt = new byte[0];
		
		try {
			
			/*
			 * Per autenticare l'utente vengono eseguiti i seguenti step:
			 * 
			 * - Recupero del valore del salt associato all'utente
			 * - Generazione del valore hash della concatenazione di password e salt
			 * - Ricerca dell'utente tramite il valore della password
			 * 
			 * */
			
			userSalt = SaltDAO.findSaltByUsername(username);
			
			hashedPasswordAndSalt = PasswordUtils.generateHash(password, userSalt, prop.getProperty(Constants.HASHING_ALGORITHM));
			
			loggedUser = PasswordDAO.findUserByPassword(hashedPasswordAndSalt) ? true : false;
			
		} catch (Exception e) {

			log.error("Eccezione in UserDAO: " + e.getMessage());
			return false;
		
		} finally {
			
			PasswordUtils.clearArray(userSalt);
			PasswordUtils.clearArray(hashedPasswordAndSalt);
			
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
					
					System.getenv(Constants.JDBC_URL) + System.getenv(Constants.USERS_DB_NAME), 
					System.getenv(Constants.USERS_DB_USERNAME), 
					System.getenv(Constants.USERS_DB_PASSWORD)
					
			);
			
			preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
			
			preparedStatement.setString(1, username);
			
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next())
				users.add(new User(resultSet.getInt("id"), resultSet.getString("username")));
			
			if(users.isEmpty())
				return new User();
			else
				return users.get(0);
			
		} catch (Exception e) {

			log.error("Eccezione in UserDAO: " + e.getMessage());
			return new User();
		
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
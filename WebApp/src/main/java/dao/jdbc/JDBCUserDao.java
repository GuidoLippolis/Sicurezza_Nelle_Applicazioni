package dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Optional;

import dao.DaoFactory;
import dao.PasswordDao;
import dao.PasswordsDaoFactoryCreator;
import dao.UserDao;
import model.User;
import passwordUtils.PasswordUtils;

/*
 * Classe che implementa i metodi dell'interfaccia UserDao tramite l'utilizzo
 * della specifica libreria JDBC.
 * 
 * */

/*
 * TODO: Recuperare le query SQL da un file .properties
 * 
 * */

public class JDBCUserDao implements UserDao {

	private Connection connection;
	
	public JDBCUserDao(Connection connection) {
		
		super();
		this.connection = connection;
		
	}
	
	@Override
	public Optional<User> signUp(User user, String password) throws SQLException {
		
		/*
		 * preparedStatementUsers serve per l'inserimento dell'utente nel database
		 * 
		 * */
		
		PreparedStatement preparedStatementUsers = null;
		
		int rowsAffectedUsers = 0;
		
//		/*
//		 * preparedStatementPasswords serve per l'inserimento del valore hash
//		 * della password nel database
//		 * 
//		 * */
//		
		PreparedStatement preparedStatementPasswords = null;
		
		ResultSet generatedKeys = null;
		
		int rowsAffectedPasswords = 0;
		
		boolean insertedPassword = false;
		
		int generatedId = 0;
		
		try {
			
			preparedStatementUsers = connection.prepareStatement("INSERT INTO users(email, photo) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
			
//			connection.setAutoCommit(false);
			
			preparedStatementUsers.setString(1, user.getEmail());
			preparedStatementUsers.setBytes(2, user.getPhoto());
			
			rowsAffectedUsers = preparedStatementUsers.executeUpdate();
			
			/*
			 * Inserimento password
			 * 
			 * */
			
			if(rowsAffectedUsers > 0) {
				
				generatedKeys = preparedStatementUsers.getGeneratedKeys();
				
			    if (generatedKeys.next()) {
			    	
			    	// TODO: Recuperare il parametro di createSalt(int) da un file .properties
			    	
			    	byte[] salt = PasswordUtils.createSalt(5);
			    	
			    	byte[] hashedPassword = PasswordUtils.generateHash(password, "SHA-256", salt);
			    	
			    	generatedId = generatedKeys.getInt(1);
			    	user.setId(generatedId);
			    	
			    	insertedPassword = createConnectionToPasswordsDBAndInsert(user, hashedPassword);
			    	
			    	if(insertedPassword) {
			    		
//			    		connection.commit();
			    		return Optional.ofNullable(user);
			    		
			    	}
			    	
			    } else
			    	
			    	return Optional.empty();
				
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return Optional.empty();
			
		} finally {
			
			if(generatedKeys != null)
				generatedKeys.close();
			
			if(preparedStatementUsers != null)
				preparedStatementUsers.close();
			
			if(preparedStatementPasswords != null)
				preparedStatementPasswords.close();
			
		}
			
		return Optional.ofNullable(user);
		
	}
	
	private boolean createConnectionToPasswordsDBAndInsert(User user, byte[] hashedPassword) throws SQLException {
		
		boolean insertedPassword = false;
		
		try {
			
			DaoFactory passwordsDaoFactoryCreator = PasswordsDaoFactoryCreator.getDaoFactory();
			PasswordDao passwordDao = passwordsDaoFactoryCreator.getPasswordDao();
			
			insertedPassword = passwordDao.insertPasswordIntoDB(user, hashedPassword);
			
		} catch (Exception e) {

			e.printStackTrace();
			return false;
			
		}
		
		return insertedPassword;
		
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
			
			Arrays.fill(password, (byte)0);
			
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
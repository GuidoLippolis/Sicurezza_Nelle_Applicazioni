package dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Optional;

import dao.DaoFactory;
import dao.SaltDao;
import dao.SaltsDaoFactoryCreator;
import dao.UserDao;
import model.Salt;
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
		
		/*
		 * generatedKeys serve per contenere gli attributi dell'oggetto
		 * inserito nel database
		 * 
		 * */
		
		ResultSet generatedKeys = null;
		
		int rowsAffectedUsers = 0;
		int rowsAffectedPasswords = 0;
		int rowsAffectedSalts = 0;
		
		/*
		 * Dopo l'inserimento di un utente nel database degli utenti (users),
		 * dal momento che la password viene memorizzata in una tabella separata
		 * per ragioni di sicurezza, è necessario recuperare l'id dell'utente generato
		 * per inserirlo nella tabella passwords
		 * 
		 * */
		
		int generatedId = 0;
		
		/*
		 * preparedStatementPasswords serve per l'inserimento del valore hash
		 * della password nel database
		 * 
		 * */
		
		PreparedStatement preparedStatementPasswords = null;
		
		/*
		 * preparedStatementSalts serve per l'inserimento del valore di salt
		 * generato nel database
		 * 
		 * */
		
		PreparedStatement preparedStatementSalts = null;
		
		if(user.getEmail().length() != 0 && password.length() != 0) {
			
			try {
				
				/*
				 * Query per l'inserimento dell'utente nel database
				 * 
				 * */
				
				preparedStatementUsers = connection.prepareStatement("INSERT INTO users(email, photo) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
				
				preparedStatementUsers.setString(1, user.getEmail());
				preparedStatementUsers.setBytes(2, user.getPhoto());
				
				rowsAffectedUsers = preparedStatementUsers.executeUpdate();
				
				if(rowsAffectedUsers > 0) {
					
					generatedKeys = preparedStatementUsers.getGeneratedKeys();
					
				    if (generatedKeys.next()) {
				    	
				    	// TODO: Recuperare il parametro di createSalt(int) da un file .properties
				    	
				    	byte[] salt = PasswordUtils.createSalt(5);
				    	
				    	byte[] hashedPassword = PasswordUtils.generateHash(password, "SHA-256", salt);
				    	
				    	generatedId = generatedKeys.getInt(1);
				    	user.setId(generatedId);
				    	
				    	/*
				    	 * Inserimento della password nel database users_passwords_db
				    	 * 
				    	 * */
				    	
				    	preparedStatementPasswords = connection.prepareStatement("INSERT INTO passwords(user_id, password) VALUES(?,?)");
				    	
				    	preparedStatementPasswords.setInt(1, generatedId);
				    	preparedStatementPasswords.setBytes(2, hashedPassword);
				    	
				        rowsAffectedPasswords = preparedStatementPasswords.executeUpdate();
				        
				        user.setSalt(new Salt(user.getId(), salt));
				        
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
				
				if(preparedStatementSalts != null)
					preparedStatementSalts.close();
				
			}
			
		} else
			
			return Optional.empty();
			
		return Optional.ofNullable(user);
		
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
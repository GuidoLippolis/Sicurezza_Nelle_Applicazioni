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
			
			/*
			 * Apertura di due connessioni diverse verso i database users_db e passwords_db
			 * 
			 * TODO: Recuperare i parametri di connessione al database da un file .properties
			 * 
			 * */
			
			passwordsDBConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/passwords_db", "root", "WgAb_9114_2359");
			usersDBConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users_db", "root", "WgAb_9114_2359");
			
			/*
			 * Si disattiva la modalità di autocommit per evitare che le singole istruzioni SQL vengano
			 * eseguite come transazioni a sé stanti. Al contrario, si raggruppano tali istruzioni in
			 * un'unica transazione, così che se una di queste dovesse fallire, falliscono tutte (rollback)
			 * 
			 * */
			
			passwordsDBConnection.setAutoCommit(false);
			
			preparedStatementUsers = usersDBConnection.prepareStatement("INSERT INTO users(email, photo) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
			
			preparedStatementUsers.setString(1, user.getEmail());
			preparedStatementUsers.setBytes(2, user.getPhoto());
			
			preparedStatementUsers.executeUpdate();
			
			/*
			 * A seguito dell'inserimento dell'utente nel database, vengono recuperati i valori dell'oggetto
			 * appena inserito. In particolare, viene recuperato il valore corrispondente all'id. Ciò viene fatto
			 * perché ogni password viene associata all'id dell'utente al quale essa appartiene
			 * 
			 * */
			
			ResultSet generatedKeys = preparedStatementUsers.getGeneratedKeys();
			
			int userId = 0;

			/*
			 * Se è stato correttamente inserito l'utente nel database, viene recuperato il suo id
			 * 
			 * */
			
			if(generatedKeys.next()) {
				
				userId = generatedKeys.getInt(1);
				
				preparedStatementPasswords = passwordsDBConnection.prepareStatement("INSERT INTO passwords(user_id, password) VALUES(?,?)");
				
				preparedStatementPasswords.setInt(1, userId);
				preparedStatementPasswords.setBytes(2, hashedPassword);
				
				preparedStatementPasswords.executeUpdate();
				
				/*
				 * Se l'inserimento della password nel database avviene senza errori, viene effettuato
				 * il commit dell'intera transazione
				 * 
				 * */
				
				passwordsDBConnection.commit();
				
				return true;
				
			}
			
		} catch (SQLException e) {
			
			/*
			 * Se si verifica qualche errore nell'inserimento di dati nel database, che l'errore
			 * riguardi il database degli utenti o quello delle password, l'intera transazione
			 * viene annullata
			 * 
			 * */
			
			if(passwordsDBConnection != null)
				passwordsDBConnection.rollback();
			
			e.printStackTrace();
			
			return false;
			
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

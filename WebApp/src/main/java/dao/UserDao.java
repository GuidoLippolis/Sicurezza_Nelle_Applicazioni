package dao;

import java.sql.SQLException;
import java.util.Optional;

import model.User;

/*
 * Interfaccia che contiene tutti i metodi utili per la gestione degli utenti
 * 
 * */

public interface UserDao {

	// Registrare un nuovo utente nel database
//	boolean signUp(User user, String password) throws SQLException;
	Optional<User> signUp(User user, String password) throws SQLException;
	
	// Controlla che le credenziali di un utente siano valide
	boolean isUserValid(String email, byte[] password) throws SQLException;
	
}

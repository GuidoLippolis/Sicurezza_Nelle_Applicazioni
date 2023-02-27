package dao;

import java.sql.SQLException;
import java.util.Optional;

import model.Salt;
import model.User;

/*
 * Interfaccia che contiene tutti i metodi utili per la gestione dei valori di salt
 * 
 * */

public interface SaltDao {

	Optional<byte[]> findSaltForUser(User user) throws SQLException;
	void insertSaltIntoDB(Salt salt) throws SQLException;
	
}

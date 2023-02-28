package dao;

import java.sql.SQLException;

import model.User;

/*
 * Interfaccia che contiene tutti i metodi utili per la gestione dei valori di salt
 * 
 * */

public interface PasswordDao {

	boolean insertPasswordIntoDB(User user, byte[] hashedPassword) throws SQLException;
	
}

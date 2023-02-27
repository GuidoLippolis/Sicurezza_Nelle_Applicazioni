package dao;

import java.sql.SQLException;

public interface LoginDao {

	boolean signUp(String email, String password, byte[] photo) throws SQLException;
	boolean isUserValid(String email, byte[] password) throws SQLException;
	
}

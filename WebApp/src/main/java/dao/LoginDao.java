package dao;

import java.sql.SQLException;

import model.User;

public interface LoginDao {

	boolean signUp(User user, String password) throws SQLException;
	boolean isUserValid(String email, byte[] password) throws SQLException;
	
}

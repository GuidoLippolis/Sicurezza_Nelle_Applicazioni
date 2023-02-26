package dao;

import java.sql.SQLException;

public interface LoginDao {

	boolean isUserValid(String email, byte[] password) throws SQLException;
	
}

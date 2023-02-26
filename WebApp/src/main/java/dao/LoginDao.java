package dao;

public interface LoginDao {

	boolean isUserValid(String email, byte[] password);
	
}

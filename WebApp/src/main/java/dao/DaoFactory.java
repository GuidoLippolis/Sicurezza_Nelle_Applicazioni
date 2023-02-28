package dao;

public interface DaoFactory extends AutoCloseable {

	UserDao getUserDao();
	PasswordDao getPasswordDao();
	SaltDao getSaltDao();
	
}

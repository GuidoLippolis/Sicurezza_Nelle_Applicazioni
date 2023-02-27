package dao;

public interface DaoFactory extends AutoCloseable {

	UserDao getUserDao();
	SaltDao getSaltDao();
	
}

package dao;

public interface DaoFactory extends AutoCloseable {

	LoginDao getLoginDao();
	
}

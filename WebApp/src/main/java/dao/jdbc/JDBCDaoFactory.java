package dao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import dao.DaoFactory;
import dao.PasswordDao;
import dao.SaltDao;
import dao.UserDao;

public class JDBCDaoFactory implements DaoFactory {

	private Connection connection;
	
	public JDBCDaoFactory(String connectionUrl, String username, String password) throws SQLException {
		
		this.connection = DriverManager.getConnection(connectionUrl, username, password);
		
	}
	
	@Override
	public void close() throws Exception {
		
		if(connection != null)
			connection.close();
		
	}

	@Override
	public UserDao getUserDao() {
		
		return new JDBCUserDao(connection);
		
	}

	@Override
	public SaltDao getSaltDao() {
		
		return new JDBCSaltDao(connection);
		
	}

	@Override
	public PasswordDao getPasswordDao() {
		
		return new JDBCPasswordDao(connection);
		
	}

}

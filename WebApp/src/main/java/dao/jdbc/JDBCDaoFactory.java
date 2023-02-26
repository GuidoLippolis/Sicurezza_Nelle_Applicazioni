package dao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import dao.DaoFactory;
import dao.LoginDao;

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
	public LoginDao getLoginDao() {
		
		return new JDBCLoginDao(connection);
		
	}

}

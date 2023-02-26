package dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import dao.LoginDao;

public class JDBCLoginDao implements LoginDao {

	private Connection connection;
	
	public JDBCLoginDao(Connection connection) {
		
		super();
		this.connection = connection;
		
	}

	@Override
	public boolean isUserValid(String email, byte[] password) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
package dao.jdbc;

import java.sql.Connection;

import dao.LoginDao;

public class JDBCLoginDao implements LoginDao {

	private Connection connection;
	
	public JDBCLoginDao(Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public boolean isUserValid(String email, byte[] password) {
		// TODO Auto-generated method stub
		return false;
	}

}
package dao;

import dao.jdbc.JDBCDaoFactory;

public class PasswordsDaoFactoryCreator {

	private static final DaoFactory instance;
	
	static {
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			instance = new JDBCDaoFactory("jdbc:mysql://localhost:3306/passwords_db", "root", "WgAb_9114_2359");
			
		} catch (Exception e) {
			
			throw new RuntimeException();
			
		}
		
	}
	
	public static DaoFactory getDaoFactory() {
		
		return instance;
		
	}
	
}

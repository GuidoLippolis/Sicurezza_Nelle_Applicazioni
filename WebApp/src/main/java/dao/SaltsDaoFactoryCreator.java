package dao;

import dao.jdbc.JDBCDaoFactory;

public class SaltsDaoFactoryCreator {

	private static final DaoFactory instance;
	
	static {
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			instance = new JDBCDaoFactory("jdbc:mysql://localhost:3306/salts", "root", "WgAb_9114_2359");
			
		} catch (Exception e) {
			
			throw new RuntimeException();
			
		}
		
	}
	
	public static DaoFactory getDaoFactory() {
		
		return instance;
		
	}
	
}

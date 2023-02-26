package dao;

import dao.jdbc.JDBCDaoFactory;

public class DaoFactoryCreator {

	private static DaoFactory instance;
	
	static {
		
		try {
			
			instance = new JDBCDaoFactory("","","");
			
		} catch (Exception e) {
			
			throw new RuntimeException();
			
		}
		
	}
	
	public static DaoFactory getDaoFactory() {
		
		return instance;
		
	}
	
}

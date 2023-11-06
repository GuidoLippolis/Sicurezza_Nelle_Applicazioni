package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import constants.Constants;
import model.User;

public class CookieDAO {
	
	private static final Logger log = Logger.getLogger(CookieDAO.class);
	
	public static boolean saveCookie(String encryptedCookieValue, int maxAge, String username) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement cookiesStatement = null;
		
		try {
			
			connection = DriverManager.getConnection(
					
					System.getenv(Constants.JDBC_URL) + System.getenv(Constants.COOKIES_DB_NAME), 
					System.getenv(Constants.COOKIES_DB_USERNAME), 
					System.getenv(Constants.COOKIES_DB_PASSWORD)
					
			);
			
			connection.setAutoCommit(false);
			
			cookiesStatement = connection.prepareStatement("INSERT INTO cookies_db.cookies(cookie_value, expiration_date, username) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
			
			cookiesStatement.setString(1, encryptedCookieValue);
			cookiesStatement.setInt(2, maxAge);
			cookiesStatement.setString(3, username);
			
			int rowsAffectedCookies = cookiesStatement.executeUpdate();
			
			if(rowsAffectedCookies == 1)
				connection.commit();
			else
				connection.rollback();
			
			return true;
			
		} catch (Exception e) {

			log.error("Eccezione in CookieDAO: ", e);
			return false;
		
		} finally {
			
			if(connection != null)
				connection.close();
			
			if(cookiesStatement != null)
				cookiesStatement.close();
			
		}
		
	}
	
	public static String findUsernameByCookieValue(String value) throws SQLException, ClassNotFoundException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement cookiesStatement = null;
		
		ResultSet resultSetCookies = null;
		
		String username = null;
		
		try {
			
			connection = DriverManager.getConnection(
					
					System.getenv(Constants.JDBC_URL) + System.getenv(Constants.COOKIES_DB_NAME), 
					System.getenv(Constants.COOKIES_DB_USERNAME), 
					System.getenv(Constants.COOKIES_DB_PASSWORD)
					
			);
			
			cookiesStatement = connection.prepareStatement("SELECT username FROM cookies_db.cookies WHERE cookie_value = ?");
			
			cookiesStatement.setString(1, value);
			
			resultSetCookies = cookiesStatement.executeQuery();
			
			boolean hasNext = resultSetCookies.next();
			
			if(hasNext)
				username = resultSetCookies.getString("username");
			
			return username;
			
		} catch (Exception e) {

			log.error("Eccezione in CookieDAO: ", e);
			return "";
		
		} finally {
			
			if(connection != null)
				connection.close();
			
			if(cookiesStatement != null)
				cookiesStatement.close();
			
			if(resultSetCookies != null)
				resultSetCookies.close();
			
		}
		
	}
	
	public static String findCookieByValue(String value) throws SQLException, ClassNotFoundException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement cookiesStatement = null;
		
		ResultSet resultSetCookies = null;
		
		String outputCookieValue = null;
		
		try {
			
			connection = DriverManager.getConnection(
					
					System.getenv(Constants.JDBC_URL) + System.getenv(Constants.COOKIES_DB_NAME), 
					System.getenv(Constants.COOKIES_DB_USERNAME), 
					System.getenv(Constants.COOKIES_DB_PASSWORD)
					
			);
			
			cookiesStatement = connection.prepareStatement("SELECT cookie_value FROM cookies_db.cookies WHERE cookie_value = ?");
			
			cookiesStatement.setString(1, value);
			
			resultSetCookies = cookiesStatement.executeQuery();
			
			boolean hasNext = resultSetCookies.next();
			
			if(hasNext)
				outputCookieValue = resultSetCookies.getString("cookie_value");
			
			return outputCookieValue;
			
		} catch (Exception e) {

			log.error("Eccezione in CookieDAO: ", e);
			return "";
		
		} finally {
			
			if(connection != null)
				connection.close();
			
			if(cookiesStatement != null)
				cookiesStatement.close();
			
			if(resultSetCookies != null)
				resultSetCookies.close();
			
		}
		
	}
	
	public static boolean deleteCookieByValue(String encryptedCookieValue) throws SQLException, ClassNotFoundException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement cookiesStatement = null;
		
		try {
			
			connection = DriverManager.getConnection(
					
					System.getenv(Constants.JDBC_URL) + System.getenv(Constants.COOKIES_DB_NAME), 
					System.getenv(Constants.COOKIES_DB_USERNAME), 
					System.getenv(Constants.COOKIES_DB_PASSWORD)
					
			);
			
			connection.setAutoCommit(false);
			
			cookiesStatement = connection.prepareStatement("DELETE FROM cookies_db.cookies WHERE cookie_value = ?");
			
			cookiesStatement.setString(1, encryptedCookieValue);
			
			int rowsAffectedCookies = cookiesStatement.executeUpdate();
			
			if(rowsAffectedCookies == 1) {
				
				connection.commit();
				return true;
				
			} else {			
				
				connection.rollback();
				return false;
				
			}
			
		} catch (Exception e) {

			log.error("Eccezione in CookieDAO: ", e);
			return false;
			
		} finally {
			
			if(connection != null)
				connection.close();
			
			if(cookiesStatement != null)
				cookiesStatement.close();
			
		}
		
	}
	
	public static int findCookieExpirationTimeByCookieValue(String value) throws SQLException, ClassNotFoundException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement cookiesStatement = null;
		
		ResultSet resultSetCookies = null;
		
		int expirationTime = 0;
		
		try {
			
			connection = DriverManager.getConnection(
					
					System.getenv(Constants.JDBC_URL) + System.getenv(Constants.COOKIES_DB_NAME), 
					System.getenv(Constants.COOKIES_DB_USERNAME), 
					System.getenv(Constants.COOKIES_DB_PASSWORD)
					
			);
			
			connection.setAutoCommit(false);
			
			cookiesStatement = connection.prepareStatement("SELECT expiration_date FROM cookies_db.cookies WHERE username = ?");
			
			cookiesStatement.setString(1, value);
			
			resultSetCookies = cookiesStatement.executeQuery();
			
			boolean hasNext = resultSetCookies.next();
			
			if(hasNext)
				expirationTime = resultSetCookies.getInt("expiration_date");
			
			return expirationTime;
			
		} catch (Exception e) {

			log.error("Eccezione in CookieDAO: ", e);
			return 0;
			
		} finally {
			
			if(connection != null)
				connection.close();
			
			if(cookiesStatement != null)
				cookiesStatement.close();
			
			if(resultSetCookies != null)
				resultSetCookies.close();
			
		}
		
	}
	
}
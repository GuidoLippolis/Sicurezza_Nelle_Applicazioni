package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import enumeration.PropertiesKeys;
import model.User;
import utils.ApplicationPropertiesLoader;
import utils.EncryptionUtils;

public class CookieDAO {
	
	private static Properties prop = ApplicationPropertiesLoader.getProperties();
	
	private static final Logger log = Logger.getLogger(CookieDAO.class);

	public static boolean saveCookie(String encryptedCookieValue, int maxAge, User user) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement cookiesStatement = null;
		
		try {
			
			connection = DriverManager.getConnection(
					
					prop.getProperty(PropertiesKeys.JDCB_URL.toString()) + prop.getProperty(PropertiesKeys.COOKIES_DB_NAME.toString()), 
					prop.getProperty(PropertiesKeys.COOKIES_DB_USERNAME.toString()), 
					prop.getProperty(PropertiesKeys.COOKIES_DB_PASSWORD.toString())
					
			);
			
			connection.setAutoCommit(false);
			
			cookiesStatement = connection.prepareStatement("INSERT INTO cookies_db.cookies(cookie_value, expiration_date, user_id) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
			
			cookiesStatement.setString(1, encryptedCookieValue);
			cookiesStatement.setInt(2, maxAge);
			cookiesStatement.setInt(3, user.getId());
			
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
	
	public static String findCookieByUserId(int userId) throws Exception {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection connection = null;
		
		PreparedStatement cookiesStatement = null;
		
		ResultSet resultSetCookies = null;
		
		String outputCookieValue = null;
		
		try {
			
			connection = DriverManager.getConnection(
					
					prop.getProperty(PropertiesKeys.JDCB_URL.toString()) + prop.getProperty(PropertiesKeys.COOKIES_DB_NAME.toString()), 
					prop.getProperty(PropertiesKeys.COOKIES_DB_USERNAME.toString()), 
					prop.getProperty(PropertiesKeys.COOKIES_DB_PASSWORD.toString())
					
			);
			
			cookiesStatement = connection.prepareStatement("SELECT cookie_value FROM cookies_db.cookies WHERE user_id = ?");
			
			cookiesStatement.setInt(1, userId);
			
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
	
}
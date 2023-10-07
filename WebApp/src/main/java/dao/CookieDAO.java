package dao;

import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import enumeration.PropertiesKeys;
import jdk.internal.org.jline.utils.Log;
import model.User;
import servlets.SignUpServlet;
import utils.PasswordUtils;

public class CookieDAO {
	
	private static Properties prop;
	
	private static final Logger log = Logger.getLogger(CookieDAO.class);

	static {
		
        try {
			
            prop = new Properties();
            
            FileInputStream in = new FileInputStream(System.getenv(PropertiesKeys.PATH_TO_PROPERTIES_FILE.toString()));
            
            prop.load(in);

            in.close();

		} catch (Exception e) {
			
			e.printStackTrace();
			
		}

	}
	
	public static boolean saveCookie(Cookie cookie, User user) throws ClassNotFoundException, SQLException {
		
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
			
			cookiesStatement = connection.prepareStatement("INSERT INTO cookies_db.cookies(cookie_value, user_id) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
			
			cookiesStatement.setBytes(1, cookie.getValue().getBytes());
			cookiesStatement.setInt(2, user.getId());
			
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
	
}
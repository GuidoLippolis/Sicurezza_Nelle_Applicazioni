package dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Optional;

import dao.DaoFactory;
import dao.PasswordDao;
import dao.PasswordsDaoFactoryCreator;
import dao.UserDao;
import model.Password;
import model.User;
import passwordUtils.PasswordUtils;

/*
 * Classe che implementa i metodi dell'interfaccia UserDao tramite l'utilizzo
 * della specifica libreria JDBC.
 * 
 * */

/*
 * TODO: Recuperare le query SQL da un file .properties
 * 
 * */

public class JDBCPasswordDao implements PasswordDao {

	private Connection connection;
	
	public JDBCPasswordDao(Connection connection) {
		
		super();
		this.connection = connection;
		
	}

	@Override
	public boolean insertPasswordIntoDB(User user, byte[] hashedPassword) throws SQLException {
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		boolean insertedPassword = false;
		int affectedRows = 0;
		
		try {
			
			preparedStatement = connection.prepareStatement("INSERT INTO passwords(user_id, password) VALUES(?,?)");
			
//			connection.setAutoCommit(false);
			
			preparedStatement.setInt(1, user.getId());
			preparedStatement.setBytes(2, hashedPassword);
			
			affectedRows = preparedStatement.executeUpdate();
			
			if(affectedRows > 0) {
				
				insertedPassword = true;
//				connection.commit();
				
			}
				
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
			
		} finally {
			
			if(preparedStatement != null)
				preparedStatement.close();
			
			if(resultSet != null)
				resultSet.close();
			
		}
		
		return insertedPassword;
		
	}

}
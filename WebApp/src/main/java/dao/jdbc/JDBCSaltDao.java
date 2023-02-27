package dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import dao.SaltDao;
import model.Salt;
import model.User;

/*
 * Classe che implementa i metodi dell'interfaccia SaltDao tramite l'utilizzo
 * della specifica libreria JDBC.
 * 
 * */

/*
 * TODO: Recuperare le query SQL da un file .properties
 * 
 * */

public class JDBCSaltDao implements SaltDao {

	private Connection connection;
	
	public JDBCSaltDao(Connection connection) {
		
		super();
		this.connection = connection;
		
	}

	@Override
	public Optional<byte[]> findSaltForUser(User user) throws SQLException {
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			
			preparedStatement = connection.prepareStatement("SELECT * FROM salt_user WHERE user_id = ?");
			
			preparedStatement.setInt(1, user.getId());
			
			resultSet = preparedStatement.executeQuery();
			
			return Optional.ofNullable(resultSet.getBytes("salt"));
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(resultSet != null)
				resultSet.close();
			
			if(preparedStatement != null)
				preparedStatement.close();
			
		}
		
		return Optional.empty();
		
	}

	@Override
	public void insertSaltIntoDB(Salt salt) throws SQLException {
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		int affectedRows = 0;
		
		try {
			
			preparedStatement = connection.prepareStatement("INSERT INTO salt_user(user_id, salt) VALUES (?,?)");
			
			preparedStatement.setInt(1, salt.getUserId());
			preparedStatement.setBytes(2, salt.getSaltValue());
			
			affectedRows = preparedStatement.executeUpdate();
			
			if(affectedRows == 0)
				throw new SQLException();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(resultSet != null)
				resultSet.close();
			
			if(preparedStatement != null)
				preparedStatement.close();
			
		}
		
	}
	
}
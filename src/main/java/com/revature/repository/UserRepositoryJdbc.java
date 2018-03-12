package com.revature.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.User;
import com.revature.util.ConnectionUtil;

public class UserRepositoryJdbc implements UserRepository {

	private static Logger logger = Logger.getLogger(UserRepositoryJdbc.class);
	
	/*
	 * Singleton Logic
	 */
	
	private static UserRepository repository = new UserRepositoryJdbc();
	
	private UserRepositoryJdbc() {}
	
	public static UserRepository getInstance() {
//		// We don't need this part because we are init inline
//		if(repository == null) {
//			return new UserRepositoryJdbc();
//		}
		return repository;
	}
	
	@Override
	public boolean insert(User user) {
		try(Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "INSERT INTO BANK_ACCOUNT(ACCOUNT_ID, B_FIRST_NAME, B_LAST_NAME, B_USERNAME, B_PASSWORD, B_BALANCE) VALUES(?,?,?,?,?,?)";
		
			logger.trace("Getting statement object in insert BANK_ACCOUNT.");
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(++parameterIndex,  user.getAccountId());
			statement.setString(++parameterIndex,  user.getFirstName());
			statement.setString(++parameterIndex, user.getLastName());;
			statement.setString(++parameterIndex, user.getUsername());
			statement.setString(++parameterIndex, user.getPassword());
			statement.setDouble(++parameterIndex, user.getBalance());
			
			logger.trace("Parameters for insertion of User set.");
			if(statement.executeUpdate() > 0) {
				logger.trace("User inserted successfully.");
				return true;
			}
		} catch (SQLException e) {
			logger.error("Exception thrown while inserting new User", e);
		}
		return false;
	}
	
	@Override
	public User findByFirstName(String firstName) {
		logger.trace("Getting User by first name.");
		try(Connection connection = ConnectionUtil.getConnection()) {
			int parameterIndex = 0;
			String sql = "SELECT * FROM CELEBRITY WHERE B_FIRST_NAME = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, firstName);
			ResultSet result = statement.executeQuery();
		
			if(result.next()) {
				return new User(
						result.getLong("BANK_ID"),
						result.getString("B_FIRST_NAME"),
						result.getString("B_LAST_NAME"),
						result.getString("B_USERNAME"),
						result.getString("B_PASSWORD"),
						result.getDouble("B_BALANCE")
						); //Copy paste from set, modify code a bit, and return first one you find
			}
			
		} catch (SQLException e) {
			logger.error("Error while selecting User by name.", e);
		}
		return null;
	}

	@Override
	public Set<User> selectAll() {
		logger.trace("Getting all Users.");
		try(Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT * FROM BANK_ACCOUNT";
			PreparedStatement statement = connection.prepareStatement(sql);
			
			ResultSet result = statement.executeQuery();
			
			Set<User> set = new HashSet<>();
			while(result.next()) {
				set.add(new User(
						result.getLong("BANK_ID"),
						result.getString("B_FIRST_NAME"),
						result.getString("B_LAST_NAME"),
						result.getString("B_USERNAME"),
						result.getString("B_PASSWORD"),
						result.getDouble("B_BALANCE")
						));
			}
			return set;
		} catch (SQLException e) {
			logger.error("Error while selecting all users.", e);
		}
		return null;
	}
	
	public static void main(String[] args) {
		UserRepository repository = new UserRepositoryJdbc();
		
//		Testing Insert
//		repository.insert(new Celebrity(10, "Beyonce", "Giselle", "Beyonce", "F"));
//		System.out.println(repository.selectAll());
		logger.info(repository.insertProcedure(new User(1, "Ismael", "Khalil", "ismaelkhalil95", "p4ssw0rd", 200)));
		logger.info(repository.selectAll());
		logger.info(repository.findByFirstName("Ismael"));
	}

	@Override
	public boolean insertProcedure(User user) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
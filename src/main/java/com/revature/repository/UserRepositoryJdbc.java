package com.revature.repository;

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

	private static Logger logger = Logger.getLogger(ConnectionUtil.class);

	private static UserRepositoryJdbc repository = new UserRepositoryJdbc();

	
	private UserRepositoryJdbc() {}
	
	public static UserRepository getInstance() {
		return repository;
	}
	
	
	@Override
	public boolean insert(User account) {
		try(Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			
			String sq1="INSERT INTO BANK_ACCOUNT(ACCOUNT_ID,B_USERNAME, B_PASSWORD, B_BALANCE ) VALUES(?,?,?,?)";
		
			logger.trace("getting statement object in insert account");
			
			PreparedStatement statement = connection.prepareStatement(sq1);
			statement.setLong(++parameterIndex, account.getuserId());
			statement.setString(++parameterIndex, account.getUsername());
			statement.setString(++parameterIndex, account.getPassword());
			statement.setDouble(++parameterIndex, account.getBalance());
			
			logger.trace("parameters for insertion of account set");
			
			if(statement.executeUpdate() > 0){
				logger.trace("account inserted succefully");
				return true;
			}
		} catch (SQLException e) {
			logger.error("exception thrown while inserting ", e);
		}
		
		
		return false;
	}

	@Override
	public boolean insertProcedure(User account) {
		return false;
	}
	
	
	

	@Override
	public Set<User> selectAll() {
		try(Connection connection = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM BANK_ACCOUNT";
			
			logger.trace("getting statement obj in select all accounts");
			
			PreparedStatement statement = connection.prepareStatement(sql);
			
			ResultSet result = statement.executeQuery();
			
			Set<User> set = new HashSet<>();
			while(result.next()) {
				set.add(new User(
						result.getLong("ACCOUNT_ID"),
						result.getString("B_USER"),
						result.getString("B_PASSWORD"),
						result.getDouble("B_BALANCE")
						));
			}
			
			return set;
			
		} catch (SQLException e) {
			
			logger.error("Error while selecting all accounts",e);
		}
		return null;
	}

	@Override
	public Set<String> selectUsers() {
		try(Connection connection = ConnectionUtil.getConnection()){
			String sql = "SELECT * FROM BANK_ACCOUNT";
			
			logger.trace("getting statement obj in select all accounts");
			
			PreparedStatement statement = connection.prepareStatement(sql);
			
			ResultSet result = statement.executeQuery();
			
			Set<String> set = new HashSet<>();
			while(result.next()) {
				set.add(new String(result.getString("B_USERNAME")));
			}
			
			return set;
			
		} catch (SQLException e) {
			
			logger.error("Error while selecting all accounts",e);
		}
		return null;
	}

	@Override
	public User findByUsername(String username) {
		try(Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			String sql = "SELECT * FROM BANK_ACCOUNT WHERE B_USERNAME = ?";
			
			logger.trace("getting statement obj in specific select accounts");
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++parameterIndex, username);
			ResultSet result = statement.executeQuery();
			
			
			if(result.next()) {
				return new User(
						result.getLong("ACCOUNT_ID"),
						result.getString("B_USERNAME"),
						result.getString("B_PASSWORD"),
						result.getDouble("B_BALANCE"));
			}
			
			
			
		} catch (SQLException e) {
			
			logger.error("Error while selecting account by user name",e);
		}
		return null;
	}

	@Override
	public boolean updateAccountBalance(User account) {
		try(Connection connection = ConnectionUtil.getConnection()){
			int parameterIndex = 0;
			
			String sq1="UPDATE BANK_ACCOUNT SET B_BALANCE = ? WHERE B_USERNAME = ?";
		
			logger.trace("getting statement object in update account");
			
			PreparedStatement statement = connection.prepareStatement(sq1);
			statement.setDouble(++parameterIndex, account.getBalance());
			statement.setString(++parameterIndex, account.getUsername());
			
			logger.trace("parameters for update of account set");
			//System.out.println(statement.toString());
			
			
			if(statement.executeUpdate() > 0){
				logger.trace("account updated succefully");
				return true;
			}
		} catch (SQLException e) {
			logger.error("exception thrown while updating ", e);
		}
		
		
		return false;
	}
}




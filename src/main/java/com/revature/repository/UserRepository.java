package com.revature.repository;

import java.util.Set;

import com.revature.model.User;

/**
 * User entity DAO
 *
 */

public interface UserRepository {
	
	/**
	 * Creates/Inserts a new User in the database
	 */
	public boolean insert(User user);
	/**
	 * Insets a new User in the database using CallableStatement
	 * (Stored Procedure)
	 */
	public boolean insertProcedure(User user);
	/**
	 * Select one [first] User based on his/her first name.
	 */
	public User findByFirstName(String firstName);
	
	/**
	 * Select all Users from the database.
	 * @return
	 */
	public Set<User> selectAll();
	
	
	
}

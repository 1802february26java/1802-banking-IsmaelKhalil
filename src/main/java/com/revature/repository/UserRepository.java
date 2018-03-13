package com.revature.repository;

import java.util.Set;

import com.revature.model.User;

public interface UserRepository {

	
	public boolean insert(User account);
	
	public boolean insertProcedure(User account);
	
	public Set<String> selectUsers();
	
	public User findByUsername(String username);
	
	public Set<User> selectAll();
	
	public boolean updateAccountBalance(User account);
	
	
}

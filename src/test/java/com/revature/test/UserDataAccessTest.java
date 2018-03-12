package com.revature.test;
import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.revature.model.User;
import com.revature.repository.UserRepository;
import com.revature.repository.UserRepositoryJdbc;


/*
 * This test case's purpose is to make sure all
 * data access operations of UserRepository
 * work properly.
 * 
 * I invite you to investigate Mockito,
 * which works better for testing data access code.
 * 
 * With Mockito you can create MOCK: Connections, ResultSets
 * We are going to test against the real database
 * only the read operations.
 * 
 * Another way without Mockito is to have a static TEST database.
 * 
 */



public class UserDataAccessTest {

	private static Logger logger = Logger.getLogger(UserDataAccessTest.class);
	
	private UserRepository repository;
	
	// Mock objects
	private User userTest;
	
	//Will execute before every @Test
	@Before
	public void setUp() {
		repository = UserRepositoryJdbc.getInstance();
		userTest = new User(1, "Ismael", "Khalil", "ismaelkhalil95", "p4ssw0rd", 200);
	}
	
	//Unit test
	@Test
	public void selectAllTest() {
		logger.trace("Testing selectAll users.");
		assertEquals(repository.selectAll().size(), 4);
	}
	
	//Unit test
	@Test
	public void findByNameTest() {
		logger.trace("Testing findByName user.");
		assertEquals(repository.findByFirstName("Ismael"), userTest);
	}
}

package banking;

import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.revature.model.User;
import com.revature.repository.UserRepository;
import com.revature.repository.UserRepositoryJdbc;
import com.revature.util.ConnectionUtil;

public class UserLoginTest {

	private static Logger logger = Logger.getLogger(ConnectionUtil.class);
	UserRepository repository;
	
	//testing obj
	private User dummyAccount;
	
	@Before
	public void setup(){
		repository = UserRepositoryJdbc.getInstance();
	}
	
	//Unit Tests
	@Test
	public void izzyBalanceTest(){
		logger.trace("testing updating admin balance.");
		dummyAccount = new User(7,"mobydick","p4ssw0rd", 1000000);
		assertTrue(repository.updateAccountBalance(dummyAccount));
	}
	
	@Test
	public void testGetUser(){
		izzyBalanceTest();
		equals(repository.findByUsername("mobydick"));
	}
}

package com.revature.service;

import org.apache.log4j.Logger;

import com.revature.exception.RejectCredentialsException;
import com.revature.exception.BounceException;
import com.revature.model.User;
import com.revature.repository.UserRepository;
import com.revature.repository.UserRepositoryJdbc;

public class ServiceUtil {

	private static ServiceUtil service = new ServiceUtil();

	private UserRepository repository = UserRepositoryJdbc.getInstance();

	private static Logger logger = Logger.getLogger(ServiceUtil.class);

	private User loggedAccount = null;
	
	
	private ServiceUtil(){}
	
	public static ServiceUtil getInstance(){
		return service;
	}
	
	public User getAccount() {
		return loggedAccount;
	}
	
	
	public User login(String user, String password) throws RejectCredentialsException{
		if(repository.findByUsername(user) != null){
			User account = repository.findByUsername(user);

			if(account.getPassword().equals(password)){
				loggedAccount = account;

				System.out.println("You are now logged in");
				return account;
			}
		} else {
			throw new RejectCredentialsException("Invalid credentials, please try again.");
		}
		return null;
	}
	
	public boolean logout() {
		if(loggedAccount == null){
			System.out.println("Please log in and try again.");
			return false;
		} else {
			loggedAccount = null;
			System.out.println("Successfully logged out. Have a good day!");
			return true;
		}
	}
	
	public boolean register(String user, String password){
		
		if(repository.insert(new User(0, user, password, 0))) {
			return true;
		}
		
		return false;
	}
	
	public double getBalance(){
		return loggedAccount.getBalance();
		
	}
	
	
	public boolean deposit(double plus)  {
		if(loggedAccount == null){
			System.out.println("Please log in and try again.");
			return false;
		} else if (plus <= 0) {
			System.out.println("Invalid amount. Please try again.");
		} else {
				loggedAccount.deposit(plus);
				repository.updateAccountBalance(loggedAccount);
				completedTransaction();
				return true;
		}
		
		return false;
	}
	
	public boolean withdraw(double minus) throws BounceException{
		if(loggedAccount == null){
			System.out.println("Please log in and try again.");
			return false;
		} else if (minus <= 0) {
			System.out.println("Invalid amount. Please try again.");
		} else {
			try {
				loggedAccount.withdraw(minus);
				repository.updateAccountBalance(loggedAccount);
				completedTransaction();
				return true;
				
			} catch (BounceException e) {
				System.out.println(e.getMessage());
			}
		}
		
		return false;
	}
	
	private void completedTransaction(){
		System.out.println("Thank you "+ loggedAccount.getUsername() + ", have a nice day!");
		System.out.println("You now have " + loggedAccount.getBalance() + " in your account.");
	}

	
}

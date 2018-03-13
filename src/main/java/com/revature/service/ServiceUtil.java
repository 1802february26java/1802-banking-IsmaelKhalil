package com.revature.service;

import org.apache.log4j.Logger;

import com.revature.exception.InvalidCredentialException;
import com.revature.exception.BounceException;
import com.revature.model.User;
import com.revature.repository.UserRepository;
import com.revature.repository.UserRepositoryJdbc;

public class ServiceUtil {

	private static ServiceUtil service = new ServiceUtil();

	private UserRepository repository = UserRepositoryJdbc.getInstance();

	private static Logger logger = Logger.getLogger(ServiceUtil.class);

	private User currentAccount = null;
	
	
	private ServiceUtil(){}
	
	public static ServiceUtil getInstance(){
		return service;
	}
	
	public User getAccount() {
		return currentAccount;
	}
	
	
	public User login(String user, String password) throws InvalidCredentialException{
		if(repository.findByUsername(user) != null){
			User account = repository.findByUsername(user);

			if(account.getPassword().equals(password)){
				currentAccount = account;

				System.out.println("You are now logged in");
				return account;
			}
		} else {
			throw new InvalidCredentialException("Invalid credentials, please try again.");
		}
		return null;
	}
	
	public boolean logout() {
		if(currentAccount == null){
			System.out.println("Nobody is logged in");
			return false;
		} else {
			currentAccount = null;
			System.out.println("Logout successful");
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
		return currentAccount.getBalance();
		
	}
	
	
	public boolean deposit(double depo)  {
		if(currentAccount == null){
			System.out.println("You must login first");
			return false;
		} else if (depo <= 0) {
			System.out.println("Please enter a valid deposit amount");
		} else {
				currentAccount.deposit(depo);
				repository.updateAccountBalance(currentAccount);
				printRecipt();
				return true;
		}
		
		return false;
	}
	
	public boolean withdraw(double withd) throws BounceException{
		if(currentAccount == null){
			System.out.println("You must login first");
			return false;
		} else if (withd <= 0) {
			System.out.println("Please enter a valid withdraw amount");
		} else {
			try {
				currentAccount.withdraw(withd);
				repository.updateAccountBalance(currentAccount);
				printRecipt();
				return true;
				
			} catch (BounceException e) {
				System.out.println(e.getMessage());
			}
		}
		
		return false;
	}
	
	private void printRecipt(){
		System.out.println("----------------------------------------");
		System.out.println("| Recipt for account "+ currentAccount.getUsername() + " |");
		System.out.println("| Current Balance is: " + currentAccount.getBalance() + " |");
		System.out.println("----------------------------------------");
	}

	
}

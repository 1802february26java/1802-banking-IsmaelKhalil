package com.revature.controller;

import java.util.HashMap;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.exception.InvalidCredentialException;
import com.revature.exception.InvalidInputException;
import com.revature.exception.BounceException;
import com.revature.model.User;
import com.revature.repository.UserRepository;
import com.revature.repository.UserRepositoryJdbc;
import com.revature.service.ServiceUtil;
import com.revature.util.ConnectionUtil;

public class Controller {

	private static final Controller controller= new Controller();
	UserRepository repository = UserRepositoryJdbc.getInstance();
	ServiceUtil service = ServiceUtil.getInstance();
	private static Logger logger = Logger.getLogger(ConnectionUtil.class);


	private HashMap<String, User> accounts;
	private User currentAccount;
	
	
	public static Controller getInstance(){
		return controller;
	}
	
	
	public void openBank(){
		System.out.println("This bank is now open, what would you like to do?");
		Scanner input = new Scanner(System.in);
		while(input.hasNext()){
			try {
				String[] command = input.nextLine().split(" ");
				command[0] = command[0].toLowerCase();
				switch(command[0]){
				case "login":
					if(command.length==3){
						try {
							currentAccount = ServiceUtil.getInstance().login(command[1].toLowerCase(), command[2]);
						} catch (InvalidCredentialException e) {
							System.out.println(e.getMessage());
						}

					} else {
						throw new InvalidInputException();
					}
					break;
				case "deposit":
					if(command.length == 2){
						try{
							service.deposit(Double.parseDouble(command[1]));
						}catch (NumberFormatException e) {
							System.out.println("Please enter a number after deposit when trying to make a deposit.");
						}
					} else {
						throw new InvalidInputException();
					}
					break;
				case "withdraw":
					if(command.length==2){
						try{
							service.withdraw(Double.parseDouble(command[1]));
						} catch (BounceException e) {
							System.out.println(e.getMessage());
						}catch (NumberFormatException e) {
							System.out.println("Please enter a number after withdraw when trying to make a withdrawl.");
						}
					}
					break;

				case "view":
					if(currentAccount==null){
						System.out.println("Please login first.");
					} else {
						System.out.println("Your current balance is: " +service.getBalance());
					}
					break;

				case "logout":
					currentAccount = null;
					break;

				case "register":
					if(command.length==3) {
						if(currentAccount != null) {
							System.out.println("Please logout first.");
						} else {
							if(service.getInstance().register(command[1].toLowerCase(), command[2])){
								System.out.println("Registration successful. Please try logging in.");
							} else {
								System.out.println("Registration failed, please try again.");
							}
						}
					} else {
						System.out.println("invalid.");
					}
					break;
				case "help":
					System.out.println("available commands: ");
					System.out.println("login [username] [password]");
					System.out.println("deposit [amount]");
					System.out.println("withdraw [amount]");
					System.out.println("exit");
					break;

				case "exit":
					if(currentAccount != null){
						service.logout();
					}
					System.out.println("Exiting program now.");
					System.exit(0);
					break;
				default:
					//				System.out.println("invalid request, please try again");
					throw new InvalidInputException();
				}
				System.out.println("How can I help you now?");
			} catch (InvalidInputException e) {
				System.out.println(e.getMessage());
			}
		}
		input.close();
	}
}

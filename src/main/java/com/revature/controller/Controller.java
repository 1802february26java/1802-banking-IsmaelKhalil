package com.revature.controller;

import java.util.HashMap;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.exception.RejectCredentialsException;
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
	private User loggedAccount;
	
	
	public static Controller getInstance(){
		return controller;
	}
	
	
	public void openBank(){
		System.out.println("Welcome to Khalil Banking. Please log in or register to proceed further.");
		Scanner input = new Scanner(System.in);
		while(input.hasNext()){
			try {
				String[] command = input.nextLine().split(" ");
				command[0] = command[0].toLowerCase();
				switch(command[0]){
				case "login":
					if(command.length == 3){
						try {
							loggedAccount = ServiceUtil.getInstance().login(command[1].toLowerCase(), command[2]);
						} catch (RejectCredentialsException e) {
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
							System.out.println("Invalid character detected. Please do not enter a $ sign; only enter numbers.");
						}
					} else {
						throw new InvalidInputException();
					}
					break;
				case "withdraw":
					if(command.length == 2){
						try{
							service.withdraw(Double.parseDouble(command[1]));
						} catch (BounceException e) {
							System.out.println(e.getMessage());
						}catch (NumberFormatException e) {
							System.out.println("Invalid character detected. Please do not enter a $ sign; only enter numbers.");
						}
					}
					break;

				case "view":
					if(loggedAccount == null){
						System.out.println("Please log in and try again.");
					} else {
						System.out.println("Your current balance is: " + service.getBalance());
					}
					break;

				case "logout":
					loggedAccount = null;
					break;

				case "register":
					if(command.length == 3) {
						if(loggedAccount != null) {
							System.out.println("Already logged in. Please log out to do that.");
						} else {
							if(service.getInstance().register(command[1].toLowerCase(), command[2])){
								System.out.println("Registration successful. Please try logging in.");
							} else {
								System.out.println("Registration failed, please try again.");
							}
						}
					} else {
						System.out.println("Invalid characters.");
					}
					break;
				case "commands":
					System.out.println("Commands: ");
					System.out.println("To view commands: You're here, aren't you?");
					System.out.println("To log in: login [username] [password]");
					System.out.println("To deposit: deposit [amount]");
					System.out.println("To withdraw: withdraw [amount]");
					System.out.println("To close the application: exit");
					break;

				case "exit":
					if(loggedAccount != null){
						service.logout();
					}
					System.out.println("Exiting program now. Have a good day!");
					System.exit(0);
					break;
				default:
					throw new InvalidInputException();
				}
				System.out.println("What do you need?");
			} catch (InvalidInputException e) {
				System.out.println(e.getMessage());
			}
		}
		input.close();
	}
}

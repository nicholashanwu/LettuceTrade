package com.fdmgroup.Lettuce.Service;

import java.util.List;

import com.fdmgroup.Lettuce.Exceptions.DuplicatedUserNameException;
import com.fdmgroup.Lettuce.Exceptions.FailToLoginException;
import com.fdmgroup.Lettuce.Exceptions.InvalidUserNameException;
import com.fdmgroup.Lettuce.Models.User;
/**
 * 
 * @author Bo
 *
 */
public interface iUser {

	//basic
	User getUserById(int userId);
	
	/**
	 * 
	 * @param userName
	 * @return User
	 * @throws InvalidUserNameException if can not find a user with this user name.
	 */
	User getUserByUserName(String userName) throws InvalidUserNameException;
	
	/**
	 * Add a new user in database. It will check whether the user name is duplicated
	 * @param user
	 * @throws DuplicatedUserNameException if the user name has been used.
	 */
	void addUser(User user) throws DuplicatedUserNameException;
	
	/**
	 * 
	 * @param userName
	 * @param password
	 * @return true if log in successfully
	 * @throws FailToLoginException if can not find a user with given userName and password
	 */
	boolean loginWithUserNameAndPassword(String userName, String password) throws FailToLoginException;
	
	/**
	 * 
	 * @param userName
	 * @return true if the user with this user name is an admin
	 * @throws InvalidUserNameException if can not find a user with this user name.
	 */
	boolean isAdmin(String userName) throws InvalidUserNameException;
	//TODO
//	List<Order> getOrdersById(int userId);
//	
//	Portfolio getProfileById(int userId);
	/**
	 * 
	 * @param userId
	 * @param user with updated information
	 * @throws DuplicatedUserNameException if the new user name has been used
	 */
	void updateUser(int userId, User user) throws DuplicatedUserNameException;
	
	//functions
	//TODO
//	void placeOrder(int userId, Order order);
	
	void takeMoneyFromBank(int userId, double moneyOut);
	void sendMoneyToBank(int userId, double moneyIn);
}

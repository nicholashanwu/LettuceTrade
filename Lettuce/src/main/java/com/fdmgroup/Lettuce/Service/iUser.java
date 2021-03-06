package com.fdmgroup.Lettuce.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.dao.DataIntegrityViolationException;

import com.fdmgroup.Lettuce.Exceptions.DuplicatedEmailException;
import com.fdmgroup.Lettuce.Exceptions.FailToLoginException;
import com.fdmgroup.Lettuce.Exceptions.InvalidEmailException;
import com.fdmgroup.Lettuce.Exceptions.UserNotFoundException;
import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.Portfolio;
import com.fdmgroup.Lettuce.Models.User;
/**
 * 
 * @author Bo Hunter
 *
 */
public interface iUser {

	//basic
	User getUserById(int userId);
	
	/**
	 * 
	 * @param email
	 * @return User
	 * @throws InvalidEmailException if can not find a user with this email.
	 */
	User getUserByEmail(String email) throws InvalidEmailException;
	
	/**
	 * Add a new user in database. It will check whether the email is duplicated
	 * @param user
	 * @throws DuplicatedEmailException if the email has been used.
	 */
	
	
	void addUser(User user) throws DuplicatedEmailException;
	
	/**
	 * 
	 * @param email
	 * @param password
	 * @return true if log in successfully
	 * @throws FailToLoginException if can not find a user with given email and password
	 */
	boolean loginWithEmailAndPassword(String email, String password) throws FailToLoginException;
	
	/**
	 * 
	 * @param email
	 * @return true if the user with this email is an admin
	 * @throws InvalidEmailException if can not find a user with this email.
	 */
	boolean isAdmin(String email) throws InvalidEmailException;

	List<Order> getOrdersById(int userId);
	
	Portfolio getProfileById(int userId);
	/**
	 * 
	 * @param userId
	 * @param user with updated information
	 * @throws DuplicatedEmailException if the given email has been used
	 */
	void updateUser(int userId, User user) throws DataIntegrityViolationException;
	
	//functions
	void placeOrder(int userId, Order order);
	
	/**
	 * 
	 * @param user
	 * @param siteURL
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @throws DuplicatedEmailException 
	 */
	void registerUser(User user, String siteURL) throws UnsupportedEncodingException, MessagingException, DuplicatedEmailException;
	
	public void updatePassword(User user, String newPassword);
	
	public User findByVerificationCode(String code) throws UserNotFoundException;

	
	
	
	//void takeMoneyFromBank(int userId, double moneyOut);
	//void sendMoneyToBank(int userId, double moneyIn);
	
	
}

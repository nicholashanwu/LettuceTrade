package com.fdmgroup.Lettuce.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.Lettuce.Repo.UserRepo;
import com.fdmgroup.Lettuce.Exceptions.DuplicatedEmailException;
import com.fdmgroup.Lettuce.Exceptions.FailToLoginException;
import com.fdmgroup.Lettuce.Exceptions.InvalidEmailException;
import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.Portfolio;
import com.fdmgroup.Lettuce.Models.User;

@Service
public class UserServiceImpl implements iUser {

	@Autowired
	UserRepo userRepo;
	
	@Override
	public User getUserById(int userId) {
		User user = userRepo.getById(userId);
		return user;
	}

	@Override
	public User getUserByEmail(String email) throws InvalidEmailException {
		Optional<User> oUser = userRepo.getByEmail(email.toLowerCase());
		if(oUser.isEmpty()) {
			throw new InvalidEmailException();
		}
		return oUser.get();
	}

	@Override
	public void addUser(User user) throws DuplicatedEmailException {
		// you can just use userRepo without check if the email is duplicated
		// because the email is defined to be unique, add a user with
		// duplicated email will throw DataIntegrityViolationException
		
		String email = user.getEmail();
		//TODO validate email. Assumed to be true now
		if(!isEmailDuplicated(user.getEmail()) && isEmailValid(email)) {
			//encrypt the password
			String originPassword = user.getPassword();
			String encryptedPassword = encryptPassword(originPassword);
			user.setPassword(encryptedPassword);
			
			//because the email address is not case sensitive
			user.setEmail(email.toLowerCase());

			userRepo.save(user);
			
		};
		
	}

	private boolean isEmailValid(String email) {
		// TODO Validate the email
		return true;
	}

	private boolean isEmailDuplicated(String email) throws DuplicatedEmailException {
		Optional<User> oUser = userRepo.getByEmail(email.toLowerCase());
		if(oUser.isPresent()) {
			throw new DuplicatedEmailException();
		}
		return false;	
	}
	
	private String encryptPassword(String originPassword) {
		String encryptedPassword = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(originPassword.getBytes("UTF-8"));
			byte[] result = md.digest();
			encryptedPassword=String.format("%040x",new BigInteger(1,result));
		} catch (Exception e) {
			// error in encrypting password
			e.printStackTrace();
		}
		return encryptedPassword;
	}

	@Override
	public boolean loginWithEmailAndPassword(String inputEmail, String inputPassword) throws FailToLoginException {
		String encryptedInputPassword = encryptPassword(inputPassword);
		Optional<User> oUser = userRepo.getUserByEmailAndPassword(inputEmail.toLowerCase(), encryptedInputPassword);
		if(oUser.isEmpty()) {
			throw new FailToLoginException();
		} else {
			return true;
		}
	}


	@Override
	public boolean isAdmin(String email) throws InvalidEmailException {
		User user = this.getUserByEmail(email.toLowerCase());
		return user.isAdmin();
	}

	@Override
	public List<Order> getOrdersById(int userId) {
		User user = userRepo.getById(userId);
		List<Order> orders = user.getOrders();
		return orders;
	}

	@Override
	public Portfolio getProfileById(int userId) {
		User user = userRepo.getById(userId);
		Portfolio portfolio = user.getPortfolio();
		return portfolio;
	}

	@Override
	public void updateUser(int userId, User user) throws DuplicatedEmailException {
		//because the email address is not case sensitive
		String email = user.getEmail();
		user.setEmail(email.toLowerCase());
		user.setUserId(userId);
		addUser(user);
	}

	@Override
	public void placeOrder(int userId, Order order) {
		User user = userRepo.getById(userId);
		user.getOrders().add(order);
		userRepo.save(user);
	}

	@Override
	public void takeMoneyFromBank(int userId, double moneyOut) {
		User user = userRepo.getById(userId);
		double moneyInBank = user.getBankAccountBalance();
		user.setBankAccountBalance(moneyInBank-moneyOut);
		userRepo.save(user);	
	}

	@Override
	public void sendMoneyToBank(int userId, double moneyIn) {
		User user = userRepo.getById(userId);
		double moneyInBank = user.getBankAccountBalance();
		user.setBankAccountBalance(moneyInBank+moneyIn);
		userRepo.save(user); 
	}
	
}

package com.fdmgroup.Lettuce.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.Lettuce.Repo.UserRepo;
import com.fdmgroup.Lettuce.Exceptions.DuplicatedUserNameException;
import com.fdmgroup.Lettuce.Exceptions.FailToLoginException;
import com.fdmgroup.Lettuce.Exceptions.InvalidUserNameException;
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
	public User getUserByUserName(String userName) throws InvalidUserNameException {
		Optional<User> oUser = userRepo.getByUserName(userName);
		if(oUser.isEmpty()) {
			throw new InvalidUserNameException();
		}
		return oUser.get();
	}

	@Override
	public void addUser(User user) throws DuplicatedUserNameException {
		// you can just use userRepo without check if the user name is duplicated
		// because the user name is defined to be unique, add a user with
		// duplicated user name will throw DataIntegrityViolationException
		
		if(!isUserNameDuplicated(user.getUserName())) {
			//TODO encrypt the password
//			String originPassword = user.getPassword();
//			String encryptedPassword = encryptPassword(originPassword);
//			user.setPassword(encryptedPassword);
			userRepo.save(user);
		};
		
	}

	private boolean isUserNameDuplicated(String userName) throws DuplicatedUserNameException {
		Optional<User> oUser = userRepo.getByUserName(userName);
		if(oUser.isPresent()) {
			throw new DuplicatedUserNameException();
		}
		return false;	
	}
	
	private String encryptPassword(String originPassword) {
		//TODO add a encryption strategy
		return null;
	}

	@Override
	public boolean loginWithUserNameAndPassword(String userName, String inputPassword) throws FailToLoginException {
        //TODO encrypt the password
//		String encryptedInputPassword = encryptPassword(inputPassword);
//		Optional<User> oUser = userRepo.getUserByUserNameAndPassword(userName, encryptedInputPassword);
		Optional<User> oUser = userRepo.getUserByUserNameAndPassword(userName, inputPassword);
		if(oUser.isEmpty()) {
			throw new FailToLoginException();
		} else {
			return true;
		}
	}


	@Override
	public boolean isAdmin(String userName) throws InvalidUserNameException {
		User user = this.getUserByUserName(userName);
		return user.isAdmin();
	}
	//TODO
//	@Override
//	public List<Order> getOrdersById(int userId) {
//		User user = userRepo.getById(userId);
//		List<Order> orders = user.getOrders();
//		return orders;
//	}
//
//	@Override
//	public Portfolio getProfileById(int userId) {
//		User user = userRepo.getById(userId);
//		Portfolio portfolio = user.getPortfolio();
//		return portfolio;
//	}

	@Override
	public void updateUser(int userId, User user) throws DuplicatedUserNameException {
		user.setUserId(userId);
		addUser(user);
	}

	//TODO
//	@Override
//	public void placeOrder(int userId, Order order) {
//		User user = userRepo.getById(userId);
//		user.getOrders().add(order);
//		userRepo.save(user);
//		// TODO Auto-generated method stub
//	}

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

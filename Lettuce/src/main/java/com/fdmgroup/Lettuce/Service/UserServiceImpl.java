package com.fdmgroup.Lettuce.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.fdmgroup.Lettuce.Repo.UserRepo;

import net.bytebuddy.utility.RandomString;

import com.fdmgroup.Lettuce.Exceptions.DuplicatedEmailException;
import com.fdmgroup.Lettuce.Exceptions.FailToLoginException;
import com.fdmgroup.Lettuce.Exceptions.InvalidEmailException;
import com.fdmgroup.Lettuce.Exceptions.UserNotFoundException;
import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.Portfolio;
import com.fdmgroup.Lettuce.Models.User;


@Service
public class UserServiceImpl implements iUser {

	@Autowired
	UserRepo userRepo;
	@Autowired
	JavaMailSender mailSender;
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
	//For sending email verification when register.
	@Override
	public void registerUser(User user, String siteURL) throws UnsupportedEncodingException, DuplicatedEmailException, MessagingException{
		String email = user.getEmail();
		if(!isEmailDuplicated(user.getEmail()) && isEmailValid(email)) {
			String originPassword = user.getPassword();
			String encryptedPassword = encryptPassword(originPassword);
			user.setPassword(encryptedPassword);
		     
		    String randomCode = RandomString.make(16);
		    user.setVerificationCode(randomCode);
		    user.setEnabled("false");
		    user.setEmail(email.toLowerCase());
		    userRepo.save(user);
		     
		    sendVerificationEmail(user, siteURL);
		}

	}

	private void sendVerificationEmail(User user, String siteURL)
	        throws MessagingException, UnsupportedEncodingException {
	    String toAddress = user.getEmail();
	    String fromAddress = "lettucetradingteam@gmail.com";
	    String senderName = "Lettuce Group";
	    String subject = "Please verify your registration";
	    String content = "Dear [[name]],<br>"
	            + "Please click the link below to verify your registration:<br>"
	            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
	            + "Thank you,<br>"
	            + "The Lettuce Team.";
	     
	   //MimeMessage message = mailSender.createMimeMessage();
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom(fromAddress, senderName);
	    helper.setTo(toAddress);
	    helper.setSubject(subject);
	     
	    content = content.replace("[[name]]", user.getFirstName());
	    String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
	     
	    content = content.replace("[[URL]]", verifyURL);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);
	     
	}
	public boolean verify(String verificationCode) {
	    User user = userRepo.findByVerificationCode(verificationCode);
	    System.out.println(user);
	    if (user == null || user.getEnabled().equals("true")) {
	        return false;
	    } else {
	        user.setVerificationCode(null);
	        user.setEnabled("true");
	        userRepo.save(user);
	         
	        return true;
	    }
	     
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
	
	public void resetPassword(String email,String siteURL) throws UserNotFoundException, UnsupportedEncodingException, MessagingException {
		String randomCode = RandomString.make(16);
		Optional<User> userOption= userRepo.getByEmail(email);
		User user;
		if (userOption.isEmpty()) {
			throw new UserNotFoundException();
		}else {
			user=userOption.get();
			user.setVerificationCode(randomCode);
			userRepo.save(user);
			sendResetPasswordVerificationEmail(user,siteURL);
		}
		
		
		
		
	}
	private void sendResetPasswordVerificationEmail(User user, String siteURL)
	        throws MessagingException, UnsupportedEncodingException {
	    String toAddress = user.getEmail();
	    String fromAddress = "lettucetradingteam@gmail.com";
	    String senderName = "Lettuce Group";
	    String subject = "Please reset your password";
	    String content = "Dear [[name]],<br>"
	            + "Please click the link below to reset your password:<br>"
	            + "<h3><a href=\"[[URL]]\" target=\"_self\">RESET YOUR PASSWORD</a></h3>"
	            + "Thank you,<br>"
	            + "The Lettuce Team.";
	     
	   //MimeMessage message = mailSender.createMimeMessage();
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom(fromAddress, senderName);
	    helper.setTo(toAddress);
	    helper.setSubject(subject);
	     
	    content = content.replace("[[name]]", user.getFirstName());
	    String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
	     
	    content = content.replace("[[URL]]", verifyURL);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);
	     
	}
	
	public void updatePassword(User user, String newPassword) {
		String encryptedPassword = encryptPassword(newPassword);
		user.setPassword(encryptedPassword);
		user.setVerificationCode(null);
		userRepo.save(user);
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
		String adminStatus=user.isAdmin();
		
		return adminStatus.equals("yes");
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
	public void updateUser(int userId, User user) throws DataIntegrityViolationException {
		//because the email address is not case sensitive
		String email = user.getEmail();
		user.setEmail(email.toLowerCase());
		user.setUserId(userId);
		userRepo.save(user);
	}

	@Override
	public void placeOrder(int userId, Order order) {
		User user = userRepo.getById(userId);
		user.getOrders().add(order);
		userRepo.save(user);
	}



//	@Override
//	public void takeMoneyFromBank(int userId, double moneyOut) {
//		User user = userRepo.getById(userId);
//		double moneyInBank = user.getBankAccountBalance();
//		user.setBankAccountBalance(moneyInBank-moneyOut);
//		userRepo.save(user);	
//	}
//
//	@Override
//	public void sendMoneyToBank(int userId, double moneyIn) {
//		User user = userRepo.getById(userId);
//		double moneyInBank = user.getBankAccountBalance();
//		user.setBankAccountBalance(moneyInBank+moneyIn);
//		userRepo.save(user); 
//	}
//	
}

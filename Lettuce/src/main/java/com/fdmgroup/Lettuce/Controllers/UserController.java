package com.fdmgroup.Lettuce.Controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.fdmgroup.Lettuce.Exceptions.DuplicatedEmailException;
import com.fdmgroup.Lettuce.Exceptions.UserNotFoundException;
import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.HeldCurrency;
import com.fdmgroup.Lettuce.Models.Portfolio;
import com.fdmgroup.Lettuce.Models.User;
import com.fdmgroup.Lettuce.Repo.CurrencyRepo;
import com.fdmgroup.Lettuce.Repo.HeldCurrencyRepo;
import com.fdmgroup.Lettuce.Repo.PortfolioRepo;
import com.fdmgroup.Lettuce.Service.CurrencyServiceImpl;
import com.fdmgroup.Lettuce.Service.PortfolioServiceImpl;
import com.fdmgroup.Lettuce.Service.UserServiceImpl;
import com.fdmgroup.Lettuce.rates.ExchangeRate;

@Controller
@SessionAttributes({ "user" })
public class UserController {
	// In my(Bo) project, I use this way to identify the user who is using the
	// system,
	// The disadvantage is that only one user can use the system at a time
	// I didn't figure out how to solve it
	private int currentUserId;
	private boolean currentAdmin = false;

	@Autowired
	private UserServiceImpl usi = new UserServiceImpl();
	@Autowired
	private CurrencyServiceImpl csi = new CurrencyServiceImpl();
	@Autowired
	private PortfolioServiceImpl psi = new PortfolioServiceImpl();
	@Autowired
	private PortfolioRepo pr;
	@Autowired
	private HeldCurrencyRepo hcr;

	// When adding currencies to a user's portfolio, please follow this process!
	public void setUpNewTestUser(User user) throws DuplicatedEmailException {

		usi.addUser(user);
		
		
		Portfolio portfolio = new Portfolio(user);

		psi.addPortfolio(portfolio);
		
		// Create and add the popular currencies into db
		Currency currency1 = new Currency("AUD");
		Currency currency2 = new Currency("USD");
		Currency currency3 = new Currency("CAD");
		Currency currency4 = new Currency("GBP");
		Currency currency5 = new Currency("NZD");
		Currency currency6 = new Currency("EUR");
		Currency currency7 = new Currency("JPY");
	
		csi.addCurrency(currency1);
		csi.addCurrency(currency2);
		csi.addCurrency(currency3);
		csi.addCurrency(currency4);
		csi.addCurrency(currency5);
		csi.addCurrency(currency6);
		csi.addCurrency(currency7);
		
		// Let the new user have some money to use for test transactions
		HeldCurrency hc1 = new HeldCurrency(portfolio, currency1);
		HeldCurrency hc2 = new HeldCurrency(portfolio, currency2);
		HeldCurrency hc3 = new HeldCurrency(portfolio, currency3);
		HeldCurrency hc4 = new HeldCurrency(portfolio, currency4);
		HeldCurrency hc5 = new HeldCurrency(portfolio, currency5);
		HeldCurrency hc6 = new HeldCurrency(portfolio, currency6);
		HeldCurrency hc7 = new HeldCurrency(portfolio, currency7);

		hc1.setQuantity(5000.0);
		hc2.setQuantity(2500.0);
		hc3.setQuantity(500.0);
		hc4.setQuantity(12500.0);
		hc5.setQuantity(400.0);
		hc6.setQuantity(7500.0);
		hc7.setQuantity(0.0);		
		//set JPY to zero to test that it does not show up as a sellable currency in order.html
		
		hcr.save(hc1);
		hcr.save(hc2);
		hcr.save(hc3);
		hcr.save(hc4);
		hcr.save(hc5);
		hcr.save(hc6);
		hcr.save(hc7);

	}

	@ModelAttribute("user")
	public User user() {
		return new User();
	}

	@RequestMapping("/")
	public String toIndexPage(Model model) {
//		actLogger.info("Landed in Home Page")
		Map<String, Double> rates = new HashMap<String, Double>();
		String currency ="AUD";
		try {
			rates = ExchangeRate.getPopularRates("AUD");
			rates.remove(currency);
			model.addAttribute("currency",currency);
			model.addAttribute("rates",rates);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "index";
	}
	@RequestMapping("/changeRateHandler")
	public String getRateHandler(@RequestParam String currency, Model model,HttpServletRequest request) {
		
		Map<String, Double> rates = new HashMap<String, Double>();
			try {
				rates = ExchangeRate.getPopularRates(currency);
				rates.remove(currency);
				model.addAttribute("currency",currency);
				model.addAttribute("rates",rates);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return "index";
	}
	
	
	
	@RequestMapping("/login")
	public String toLoginPage() {
//		actLogger.info("Landed in login Page")
		return "login";
	}

	@RequestMapping("/register")
	public String toRegisterPage(Model model) {
		// actLogger.info("Landed in register Page")
		User user = new User();
		model.addAttribute("user", user);
		return "register";
	}
	
	@RequestMapping("/registerHandler")
	public String handlerRegister(User user) {
		try {
			// PLEASE remember the password - there are no decryption process

			setUpNewTestUser(user);

			
			//actLogger.info("Register user successfully");
			//dbLogger.info("Register user successfully");
		 
			return "register-message";
		} catch (Exception e) {
			// actLogger.warn("Fail to register a user because" + e.getMessage());
			e.printStackTrace();
			return "register";
		}
	}

	/**
	@RequestMapping("/registerHandler") 
	public String registerHandler(User user, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException, DuplicatedEmailException { 
		try { 
			usi.registerUser(user,getSiteURL(request)); 
			return "register-message"; 
		} catch (Exception e) {
			e.printStackTrace(); 
			return "register"; 
		}
	}
	 **/
	//email verification for registration
	@RequestMapping("/verify")
	public String verifyUser(@Param("code") String code) {
		//System.out.println(code);
		if (usi.verify(code)) {
			return "login";
		} else {
			return "register";
		}
	}

	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}

	@RequestMapping("/forgotPasswordForm")
	public String forgotPasswordForm() {
		return "forgot-password";
	}
	
	@RequestMapping("/forgetPasswordHandler")
	public String forgetPasswordHandler(@RequestParam(value = "email") String email,HttpServletRequest request,Model model) throws UnsupportedEncodingException, MessagingException {
		try {
			usi.resetPassword(email, getSiteURL(request));
			model.addAttribute("message","A reset password link has been sent to your email. Please use it to set up your new password");
		} catch (UserNotFoundException e) {
			 model.addAttribute("error", e.getMessage());
		}
		catch (UnsupportedEncodingException | MessagingException ex) {
			model.addAttribute("error", "We are encoutring an error while sending email. Please try again.");
		}
		
		return "forgot-password";
	}
	@RequestMapping("/reset_password")
	public String resetPassword(@Param("code") String code,Model model) {
		if(usi.verifyToken(code)) {
			model.addAttribute("token", code);
			return "reset-password";
		}else {
	        model.addAttribute("message", "Invalid Token");
	        return "message";
		}
		
	}
	@RequestMapping("/resetPasswordHandler")
	public String resetPasswordHandler(@RequestParam(value = "token") String token, @RequestParam(value = "password") String password,
			@RequestParam(value = "confirmpassword") String confrimpassword) throws UserNotFoundException {
		try {
			User user = usi.findByVerificationCode(token);
			usi.updatePassword(user, confrimpassword);
			return "login";
		} catch (Exception e) {
			return "forgot-password";
		}
		
		
		
	}
	// change the Password before login
	@RequestMapping("/changePassword")
	public String toChangePasswordPage() {
		// actLogger.info("Landed in changePassword Page")
		return "changePassword";

	}

	@RequestMapping("/changePasswordHandler")
	public String handlerChangePassword(@RequestParam(value = "email") String email,
			@RequestParam(value = "oldPassword") String oldPassword,
			@RequestParam(value = "newPassword") String newPassword,
			@RequestParam(value = "confirmPassword") String confirmPassword) {

		boolean isConfirmed = (newPassword.equals(confirmPassword));
		boolean verified;
		User user;

		try {
			verified = usi.loginWithEmailAndPassword(email, oldPassword);
			user = usi.getUserByEmail(email);
		} catch (Exception e) {
			verified = false;
			user = new User();
			/*
			 * possible situation: 1. the email do not exist 2. the password do not match
			 * the email
			 */
//		    actLogger.warn("Fail to change password because EXCEPTION " + e.getClass()+" "+e.getMessage());
		}

		if (isConfirmed && verified) {
			user.setPassword(newPassword);
			try {
				int userId = user.getUserId();
				usi.updateUser(userId, user);
//			    actLogger.info("Change password successfully");
//			    dbLogger.info("Change password successfully");
			} catch (DataIntegrityViolationException e) {
				// Normally, this exception will not be thrown because the email is not changed
			}
			return "login";
		} else {
			return "changePassword";
		}
	}

	@RequestMapping("/loginHandler")
	public String handlerLogin(@RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password, Model model) {

		boolean verified;
		boolean isAdmin;
		try {
			verified = usi.loginWithEmailAndPassword(email, password);
			isAdmin = false;
//			isAdmin=usi.isAdmin(email);											may not have admin features in final product
			currentUserId = usi.getUserByEmail(email).getUserId();
		} catch (Exception e) {
			verified = false;
			isAdmin = false;
			e.printStackTrace();
//		    actLogger.warn("Fail to login because EXCEPTION " + e.getClass()+" "+e.getMessage());
		}

		if (verified) {
			if (isAdmin) {
				currentAdmin = true;
//		        actLogger.info(" log in successfully as an admin");
//		        dbLogger.info(" log in successfully as an admin");
//		        adminLogger.info(" log in successfully as an admin");
				return "adminDashboard";
			} else {
				// the default value of currentAdmin is false

//		        actLogger.info(" log in successfully as a user");
//		        dbLogger.info(" log in successfully as a user");
//		        adminLogger.info(" log in successfully as a user");
				User user = usi.getUserById(currentUserId);
				model.addAttribute("user", user);

				return "redirect:/dashboard";
			}
		} else {
			return "login";
		}
	}

	// update the personal profile after log in
	@RequestMapping("updateProfile")
	public String toUpdateProfile(Model model) {
		User user = usi.getUserById(currentUserId);
		model.addAttribute("user", user);
//		actLogger.info("Landed in updateProfile page");
//	    userLogger.info("("+currentUserId+")Landed in updateProfile page");
		return "updateProfile";
	}

	@RequestMapping("updateProfileHandler")
	public String handlerUpdateProfile(User user) {
		try {
			usi.updateUser(currentUserId, user);
//		actLogger.info("Update user successfully");
//	    userLogger.info("("+currentUserId+")Update user successfully");
//	    dbLogger.info("Update user"+currentUserId+" successfully");
		} catch (DataIntegrityViolationException e) {
//			actLogger.info("Fail to update user");
//		   	userLogger.info("("+currentUserId+")Fail to update user");
//		    dbLogger.info("Fail to update user("+currentUserId+")");
		}
		return "userDashboard";
	}

	@RequestMapping("/logout")
	public String logOutHandler(SessionStatus status) {
		status.setComplete();
		return "index";
	}

	/*
	 * I don't know how to transfer funds from bank account to trade portfolio. It
	 * seems that it need place an order first, and then transfer the money
	 * according to the order.
	 */

}

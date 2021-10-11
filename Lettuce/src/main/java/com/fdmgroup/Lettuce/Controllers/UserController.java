package com.fdmgroup.Lettuce.Controllers;


import java.io.UnsupportedEncodingException;

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

@Controller
@SessionAttributes({"user"})
public class UserController {
	// In my(Bo) project, I use this way to identify the user who is using the system,
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
	
	//When adding currencies to a user's portfolio, please follow this process!
	public void setUpNewTestUser(User user) throws DuplicatedEmailException {
		
		usi.addUser(user);
		
	    Portfolio portfolio = new Portfolio(user);
	    
	    Currency currency1 = new Currency("USD");
		Currency currency2 = new Currency("AUD");
		Currency currency3 = new Currency("HKD");
		
		psi.addPortfolio(portfolio);		
		
		csi.addCurrency(currency1);
		csi.addCurrency(currency2);
		csi.addCurrency(currency3);
		
		hcr.save(new HeldCurrency(portfolio, currency1));
		hcr.save(new HeldCurrency(portfolio, currency2));
		hcr.save(new HeldCurrency(portfolio, currency3));
		
		// there should only be two currencies with quantities > 0
		//currently not working pls help
//		psi.increaseCurrency(currency1, 50.0, portfolio.getPortfolioId());
//		psi.increaseCurrency(currency2, 100.0, portfolio.getPortfolioId());
		
	}
	
	@ModelAttribute("user")
	public User user() {
		return new User();
	}
	
	@RequestMapping("/")
	public String toIndexPage() {
//		actLogger.info("Landed in Home Page")
		return "index";
	}
	
	@RequestMapping("/login")
	public String toLoginPage() {
//		actLogger.info("Landed in login Page")
		return "login";
	}
	
	@RequestMapping("/register")
	public String toRegisterPage(Model model){
		//actLogger.info("Landed in register Page")
		User user = new User();
		model.addAttribute("user",user);
		return "register";
	}
	
//	@RequestMapping("/registerHandler")
//	public String handlerRegister(User user) {
//		try {
//		    //PLEASE remember the password - there are no decryption process
//			
//			setUpNewTestUser(user);
//
////		      actLogger.info("Register user successfully");
////		      dbLogger.info("Register user successfully");
//	    return "register-message";  
//		} catch(Exception e) {
////			actLogger.warn("Fail to register a user because" + e.getMessage());
//	    	e.printStackTrace();
//	    	return "register";
//		}
//	}
	
	@RequestMapping("/registerHandler")
	public String registerHandler(User user, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException, DuplicatedEmailException {
		try {
			usi.registerUser(user, getSiteURL(request));
			return "register-message";
		} catch (Exception e) {
			e.printStackTrace();
			return "register";
		}
	}
	@RequestMapping("/verify")
	public String verifyUser(@Param("code") String code) {
		System.out.println(code);
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

	
	//change the Password before login
	@RequestMapping("/changePassword")
	public String toChangePasswordPage() {
		//actLogger.info("Landed in changePassword Page")
		return "changePassword";
		
	}
	
	@RequestMapping("/changePasswordHandler")
	public String handlerChangePassword(@RequestParam(value="email") String email,
                               @RequestParam(value="oldPassword") String oldPassword,
                               @RequestParam(value="newPassword") String newPassword,
                               @RequestParam(value="confirmPassword") String confirmPassword) {
		
		boolean isConfirmed = (newPassword.equals(confirmPassword));
		boolean verified;
		User user;
		
		try {
			verified=usi.loginWithEmailAndPassword(email, oldPassword);
			user = usi.getUserByEmail(email);
		} catch (Exception e) {
			verified=false;
			user = new User();
			/*
			 * possible situation:
			 * 1. the email do not exist
			 * 2. the password do not match the email 
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
				//Normally, this exception will not be thrown because the email is not changed
			}
			return "login";
		} else {
			return "changePassword";
		}
	}
	
	@RequestMapping("/loginHandler")
    public String handlerLogin(@RequestParam(value="email") String email,
                               @RequestParam(value="password") String password, Model model) {
		
		boolean verified;
		boolean isAdmin;
		try {
			verified=usi.loginWithEmailAndPassword(email, password);
			isAdmin = false;
//			isAdmin=usi.isAdmin(email);											may not have admin features in final product
			currentUserId=usi.getUserByEmail(email).getUserId();
		} catch (Exception e) {
			verified=false;
			isAdmin=false;
			e.printStackTrace();
//		    actLogger.warn("Fail to login because EXCEPTION " + e.getClass()+" "+e.getMessage());
		}
		
		if(verified) {
			if(isAdmin) {
				currentAdmin=true;
//		        actLogger.info(" log in successfully as an admin");
//		        dbLogger.info(" log in successfully as an admin");
//		        adminLogger.info(" log in successfully as an admin");
				return "adminDashboard";
			} else {
				//the default value of currentAdmin is false
				
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
		model.addAttribute("user",user);
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
		} catch(DataIntegrityViolationException e) {
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
	 * I don't know how to transfer funds from bank account to trade portfolio.
	 * It seems that it need place an order first, and then transfer the money according
	 * to the order.
	 */
	
}


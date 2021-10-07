package com.fdmgroup.Lettuce.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.Lettuce.Models.User;
import com.fdmgroup.Lettuce.Service.UserServiceImpl;

@Controller
public class UserController {
	// In my(Bo) project, I use this way to identify the user who is using the
	// system,
	// The disadvantage is that only one user can use the system at a time
	// I didn't figure out how to solve it
	private User currentUser;
	private boolean currentAdmin = false;

	@Autowired
	UserServiceImpl usi = new UserServiceImpl();



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
			user.setAdmin("False");
			usi.addUser(user);
//		      actLogger.info("Register user successfully");
//		      dbLogger.info("Register user successfully");
			return "login";
		} catch (Exception e) {
			e.printStackTrace();
//		      actLogger.warn("Fail to register a user because" + e.getMessage());
			return "register";
		}
	}

	@RequestMapping("/loginHandler")
	public String handlerLogin(@RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password) {
		
		boolean verified;
		try {
			currentUser = usi.getUserByEmail(email);
			verified = usi.loginWithEmailAndPassword(email, password);
		} catch (Exception e) {
			verified = false;
//		    actLogger.warn("Fail to login because EXCEPTION " + e.getClass()+" "+e.getMessage());
		}

		if (verified) {
			if (currentUser.isAdmin().equals("True")) {
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
				return "dashboard";
			}
		} else {
			return "login";
		}
	}

}

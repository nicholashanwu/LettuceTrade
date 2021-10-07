package com.fdmgroup.Lettuce.Controllers;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.Lettuce.Exceptions.InsufficientFundsException;
import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.Portfolio;
import com.fdmgroup.Lettuce.Models.User;
import com.fdmgroup.Lettuce.Service.PortfolioServiceImpl;
import com.fdmgroup.Lettuce.Service.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Controller
public class PortfolioController {
	@Autowired
	PortfolioServiceImpl psi = new PortfolioServiceImpl();
	@Autowired
	UserServiceImpl usi = new UserServiceImpl();
	
	public static final Logger actLogger=LogManager.getLogger("ActLogging");
	public static final Logger dbLogger=LogManager.getLogger("DBLogging");
	
//	@RequestMapping("/addPortfolio")
//	public String addPortfolioPage() {
//		return "addPortfolio";
//	}
	
//	@RequestMapping("/deletePortfolio")
//	public String deletePortfolioPage() {
//		return "deletePortfolio";
//	}
	
	@RequestMapping("/order")
	public String orderPage(@RequestParam int userId, Model model) {    // equivalent to get portfolio_id
		actLogger.info("Landed in Order page");
		
		User user = usi.getUserById(userId);
		Portfolio portfolio = new Portfolio(user);
		model.addAttribute("portfolio", portfolio);
		
		model.addAttribute("currency", new Currency());
		model.addAttribute("quantity", 0.0);
		
		return "order";
	}
	
	@RequestMapping("/addPortfolioHandler")
	public String addPortfolioHandler(@RequestParam int userId) {
		
		// suggested to applied at user registeration
		
		User user = usi.getUserById(userId);
		Portfolio portfolio = new Portfolio(user);
		psi.addPortfolio(portfolio);
		
		dbLogger.info("Added "+portfolio+" into database successfully");
		return "order";     // may require revise
	}
	
	@RequestMapping("/deletePortfolioHandler")
	public String deletePortfolioHandler(@RequestParam int portfolio_id) {
		
		// user may want to delete its portfolio
		
		psi.deletePortfolioById(portfolio_id);
		
		dbLogger.info("Deleted "+psi.getPortfolioById(portfolio_id)+" from database successfully");
		return "userDashboard";
	}
	
	@RequestMapping("/orderHandler")
	public String orderHandler(Currency currency, @RequestParam double quantity, Portfolio portfolio, @RequestParam String flag) {
		actLogger.info("Dealing with order");
		
		if(flag.equals("increase")) {
			psi.increaseCurrency(currency, quantity, portfolio.getPortfolioId());
		}else if(flag.equals("decrease")) {
			try {
				psi.decreaseCurrency(currency, quantity, portfolio.getPortfolioId());
			} catch (InsufficientFundsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "order";
	}
	
}

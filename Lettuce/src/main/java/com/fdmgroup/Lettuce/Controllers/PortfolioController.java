package com.fdmgroup.Lettuce.Controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.Portfolio;
import com.fdmgroup.Lettuce.Models.User;
import com.fdmgroup.Lettuce.Service.PortfolioServiceImpl;
import com.fdmgroup.Lettuce.Service.UserServiceImpl;

@Controller
public class PortfolioController {
	@Autowired
	PortfolioServiceImpl psi = new PortfolioServiceImpl();
	@Autowired
	UserServiceImpl usi = new UserServiceImpl();
	
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
		User user = usi.getUserById(userId);
		Portfolio portfolio = new Portfolio(user, new HashMap<>());
		model.addAttribute("portfolio", portfolio);
		
		model.addAttribute("currency", new Currency());
		model.addAttribute("quantity", 0.0);
		
		return "order";
	}
	
	@RequestMapping("/addPortfolioHandler")
	public String addPortfolioHandler(@RequestParam int userId) {
		
		// suggested to applied at user registeration
		
		User user = usi.getUserById(userId);
		Portfolio portfolio = new Portfolio(user, new HashMap<>());
		psi.addPortfolio(portfolio);
		return "order";     // may require revise
	}
	
	@RequestMapping("/deletePortfolioHandler")
	public String deletePortfolioHandler(@RequestParam int portfolio_id) {
		
		// user may want to delete its portfolio
		
		psi.deletePortfolioById(portfolio_id);
		return "userDashboard";
	}
	
	@RequestMapping("/orderHandler")
	public String orderHandler(Currency currency, @RequestParam double quantity, Portfolio portfolio, @RequestParam String flag) {
		if(flag.equals("increase")) {
			psi.increaseCurrency(currency, quantity, portfolio.getPortfolio_id());
		}else if(flag.equals("decrease")) {
			psi.decreaseCurrency(currency, quantity, portfolio.getPortfolio_id());
		}
		return "order";
	}
	
}

package com.fdmgroup.Lettuce.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.HeldCurrency;
import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.Portfolio;
import com.fdmgroup.Lettuce.Models.User;
import com.fdmgroup.Lettuce.Repo.PortfolioRepo;
import com.fdmgroup.Lettuce.Service.PortfolioServiceImpl;
import com.fdmgroup.Lettuce.Service.UserServiceImpl;
import com.fdmgroup.Lettuce.rates.ExchangeRate;

@Controller
@SessionAttributes({"user"})
public class DashboardController {

	
	@Autowired
	PortfolioServiceImpl psi = new PortfolioServiceImpl();
	@Autowired
	UserServiceImpl usi = new UserServiceImpl();
	
	
//	@RequestMapping("/")
//	public String indexPage() {
////		actLogger.info("Landed in Home Page")
//		return "index";
//	}
//	DUPLICATE


	
	@RequestMapping("/portfolio")
	public String toPortfolioPage(Model model) {
		User user = (User) model.getAttribute("user");
		Portfolio portfolio = psi.getPortfolioById(user.getPortfolio().getPortfolioId());
		model.addAttribute("portfolio",portfolio);
		
		List<HeldCurrency> heldcurrency = portfolio.getHeldCurrencies();
	
		model.addAttribute("heldCurrencys", heldcurrency); 
		return "portfolio";
	}

	
	@RequestMapping("/profile")
	public String toProfilePage(Model model) {
		User user = (User) model.getAttribute("user");
		Portfolio portfolio = psi.getPortfolioById(user.getPortfolio().getPortfolioId());
		model.addAttribute("portfolio",portfolio);
		
		List<HeldCurrency> heldcurrency = portfolio.getHeldCurrencies();
		
		model.addAttribute("heldCurrencys", heldcurrency); 
		
		return "profile";
	}
	
	@RequestMapping("/dashboard")
	public String toDashboardPage(Model model) throws IOException{
		User user = new User();
		Portfolio portfolio = new Portfolio();
				
		user = (User) model.getAttribute("user");
		portfolio = psi.getPortfolioById(user.getPortfolio().getPortfolioId());
		
		List<HeldCurrency> heldcurrency = portfolio.getHeldCurrencies();
		
		// show first 4 heldcurrency
		
		if(heldcurrency.size()>3) {
			model.addAttribute("heldCurrencys", heldcurrency.subList(0, 3)); // each has attribute currency, quantity
		}else {
			model.addAttribute("heldCurrencys", heldcurrency);
		}
//		//show the popular rates with AUD
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
		
		model.addAttribute("rates",rates);
		return "dashboard";
	}
	
	@RequestMapping("/getRateHandler")
	public String getRateHandler(@RequestParam String currency, Model model) {
		Map<String, Double> rates = new HashMap<String, Double>();
		try {
			rates = ExchangeRate.getPopularRates(currency);
			rates.remove(currency);
			model.addAttribute("currency",currency);
			model.addAttribute("rates",rates);
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return "dashboard";
	}
	
	@RequestMapping("/placeOrderHandler")
	public String toplaceOrderPage(Model model){
		Order order = new Order();
		model.addAttribute("order",order);
		return "placeOrder";
	}
	
	
	
}

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
		List<Portfolio> portfolios = psi.getAllPortfolios();
		model.addAttribute("portfolios",portfolios);
		return "portfolio";
	}
	
	@RequestMapping("/dashboard")
	public String toDashboardPage(Model model){
		//Usercontoller need to set attibrute and pass it 
		//Portfolio portfolio = usi.getUserById(userId).getPortfolio();
		

		//hardcode it just for test
		Portfolio portfolio = new Portfolio();
		portfolio.setUser(usi.getUserById(121));
		Currency c = new Currency("USD");
		HeldCurrency hc = new HeldCurrency(portfolio, c);
		hc.setQuantity(20.0);
		portfolio.getHeldCurrencies().add(hc);
		
		List<HeldCurrency> heldcurrency = portfolio.getHeldCurrencies();
		
		// show first 4 heldcurrency
		model.addAttribute("heldCurrencys", heldcurrency); // each has attribute currency, quantity


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

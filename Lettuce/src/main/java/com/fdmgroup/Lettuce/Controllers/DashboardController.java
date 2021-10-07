package com.fdmgroup.Lettuce.Controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.Portfolio;

import com.fdmgroup.Lettuce.Repo.PortfolioRepo;
import com.fdmgroup.Lettuce.Service.PortfolioServiceImpl;
import com.fdmgroup.Lettuce.rates.ExchangeRate;

@Controller
public class DashboardController {

	
	@Autowired
	PortfolioServiceImpl psi = new PortfolioServiceImpl();
	
	@RequestMapping("/portfolio")
	public String toPortfolioPage(Model model) {
		List<Portfolio> portfolios = psi.getAllPortfolios();
		model.addAttribute("portfolios",portfolios);
		return "portfolio";
	}
	
	@RequestMapping("/dashboard")
	public String toDashboardPage(Model model){
		Map<String, Double> rates = new HashMap<String, Double>();
		try {
			rates = ExchangeRate.getAllRates("USD");
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.addAttribute("rates",rates);
		return "dashboard";
	}
	
	@RequestMapping("/placeOrder")
	public String toplaceOrderPage(Model model){
		//actLogger.info("Landed in register Page")
		Order order = new Order();
		model.addAttribute("order",order);
		return "placeOrder";
	}
	
}

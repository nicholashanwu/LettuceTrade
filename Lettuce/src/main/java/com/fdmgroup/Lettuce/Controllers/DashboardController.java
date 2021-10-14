package com.fdmgroup.Lettuce.Controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.internal.build.AllowSysOut;
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
	

	
	@RequestMapping("/profile")
	public String toProfilePage(Model model) {
		User user = (User) model.getAttribute("user");
		Portfolio portfolio = psi.getPortfolioById(user.getPortfolio().getPortfolioId());
		model.addAttribute("portfolio",portfolio);
		List<HeldCurrency> heldcurrency = portfolio.getHeldCurrencies();
		printPortfolio(heldcurrency,model,false);
		
		return "profile";
	}
	
	@RequestMapping("/dashboard")
	public String toDashboardPage(Model model, HttpServletRequest request) {
		Portfolio portfolio = new Portfolio();
		User user = (User) request.getSession().getAttribute("user");
		portfolio = psi.getPortfolioById(user.getPortfolio().getPortfolioId());
		
		List<HeldCurrency> heldcurrency = portfolio.getHeldCurrencies();
	
		printPortfolio(heldcurrency,model,true);
		//show the popular rates with AUD
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
	public String getRateHandler(@RequestParam String currency, Model model,HttpServletRequest request) {
		Portfolio portfolio = new Portfolio();
		User user = (User) request.getSession().getAttribute("user");
		portfolio = psi.getPortfolioById(user.getPortfolio().getPortfolioId());
		
		List<HeldCurrency> heldcurrency = portfolio.getHeldCurrencies();
	
		
		printPortfolio(heldcurrency,model,true);
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
	
	
	public void printPortfolio(List<HeldCurrency> heldcurrency,Model model,boolean limit) {
		double totalBalance = 0;
		Map<String, Double> getAllRates = new HashMap<>();
		try {
			getAllRates = ExchangeRate.getAllRates("AUD");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<Double,HeldCurrency> portfolioWithValue = new TreeMap<>(Collections.reverseOrder());
		
		for(HeldCurrency hc:heldcurrency) {
			double exchangeRate = 0;
			try {
				exchangeRate = getAllRates.get(hc.getCurrency().getCurrencyCode());
				double value = hc.getQuantity()*((double)1/exchangeRate);
				BigDecimal bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
				portfolioWithValue.put(bd.doubleValue(),hc);
				
				totalBalance +=bd.doubleValue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		BigDecimal total = new BigDecimal(totalBalance).setScale(2, RoundingMode.HALF_UP);
		
			if(limit) {
				Map<Double,HeldCurrency> limitMap = new TreeMap<>(Collections.reverseOrder());
				
				int count=0;
				for (Double k : portfolioWithValue.keySet()) {
					if(count ==3) {
						break;
					}
					HeldCurrency value = portfolioWithValue.get(k);
				    limitMap.put(k,value);
				    count++;
				}
				model.addAttribute("heldCurrencys", limitMap);
			}else {
				model.addAttribute("heldCurrencys", portfolioWithValue);
			}
			
		model.addAttribute("TotalBalance", total.doubleValue());
		}
	}
	


package com.fdmgroup.Lettuce.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdmgroup.Lettuce.Exceptions.InsufficientFundsException;
import com.fdmgroup.Lettuce.Exceptions.InvalidDateException;
import com.fdmgroup.Lettuce.Exceptions.RecursiveTradeException;
import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.HeldCurrency;
import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.OrderStatus;
import com.fdmgroup.Lettuce.Models.OrderType;
import com.fdmgroup.Lettuce.Models.User;
import com.fdmgroup.Lettuce.Repo.HeldCurrencyRepo;
import com.fdmgroup.Lettuce.Service.CurrencyServiceImpl;
import com.fdmgroup.Lettuce.Service.OrderServiceImpl;
import com.fdmgroup.Lettuce.Service.TransactionServiceImpl;
import com.fdmgroup.Lettuce.rates.ExchangeRate;

@Controller
@SessionAttributes({"user"})
public class OrderController {
	@Autowired
	private CurrencyServiceImpl csi = new CurrencyServiceImpl();
	@Autowired
	private OrderServiceImpl osi = new OrderServiceImpl();
	@Autowired
	private TransactionServiceImpl tsi = new TransactionServiceImpl();
	@Autowired
	private HeldCurrencyRepo hcr;
	
	public static final Logger actLogger=LogManager.getLogger("ActLogging");
	public static final Logger dbLogger=LogManager.getLogger("DBLogging");
	
	// Helper method
	private Map<Currency, Double> getFilteredRates(String currencyCode) throws IOException {
		Map<String, Double> ratesAsStrings = ExchangeRate.getAllRates(currencyCode);
		Map<Currency, Double> ratesAsObjects = new HashMap<>();
		for (Map.Entry<String, Double> rate : ratesAsStrings.entrySet()) {
			try {
				Currency currency = csi.getCurrencyById(rate.getKey());
				ratesAsObjects.put(currency, rate.getValue());
			} catch (EntityNotFoundException e) {
				// API has given us a currency that we're not tracking in our database.
				// Don't add this one to the map. Do nothing.
			}
		}
		return ratesAsObjects;
	}
	
	@RequestMapping("/order")
	public String orderPage(Model model, HttpServletRequest request) {    // equivalent to get portfolio_id
		
		actLogger.info("Landed in Order page");
		
		osi.expireAll();
		tsi.resolveAllPending();
		
		User user = (User) request.getSession().getAttribute("user");
		
		if (null == user) {
			return "redirect:/";
		}
		
		List<HeldCurrency> heldCurrencies = hcr.findByPortfolio(user.getPortfolio());
		HashMap<Currency, Double> currenciesAndQuantities = new HashMap<>();
		
		// users can only sell currency that they have a positive amount of
		for (HeldCurrency hc : heldCurrencies) {
			if(hc.getQuantity() > 0) {
				currenciesAndQuantities.put(hc.getCurrency(), hc.getQuantity());
			}
		}
		
		//get map of currencies and pass them to model
		Map<Currency, Double> rates;
		
		try {
			rates = getFilteredRates("USD");
			model.addAttribute("rates", rates);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("could not get rates");
		}
		
		model.addAttribute("newOrder", new Order());
		model.addAttribute("portfolio", currenciesAndQuantities);
		model.addAttribute("currency", new Currency());
		model.addAttribute("quantity", 0.0);
		
		return "ordertemp";
	}
	
	@RequestMapping("/orderHandler")
	public String orderHandler(@RequestParam("buy-sell-choice") String choice, @RequestParam("baseCurrency") String strBaseCurrency, @RequestParam("targetCurrency") String strTargetCurrency, Order order, HttpServletRequest request, RedirectAttributes redir) {
		User user = (User) request.getSession().getAttribute("user");
		order.setUser(user);
		order.setQuantity(order.getInitialQuantity());
		System.out.println(choice);
		System.out.println(strBaseCurrency);
		System.out.println(strTargetCurrency);
		
		Currency baseCurrency;
		Currency targetCurrency;
		
		if(choice == "Sell") {					//reverse the two currencies if selling. needs testing!
			baseCurrency = csi.getCurrencyById(strTargetCurrency);
			targetCurrency = csi.getCurrencyById(strBaseCurrency);
		} else {
			baseCurrency = csi.getCurrencyById(strBaseCurrency);
			targetCurrency = csi.getCurrencyById(strTargetCurrency);
		}
		
		if(order.getScheduledDate() == null) {	//if no scheduled date, then it's a spot order
			order.setOrderType(OrderType.SPOT); 
		} else {
			order.setOrderType(OrderType.FORWARD); 
		}
		
		order.setBaseCurrency(baseCurrency);
		order.setTargetCurrency(targetCurrency);
		order.setOrderStatus(OrderStatus.ACTIVE);
		
		try {
			osi.addOrder(order);
			return "redirect:/dashboard";
		} catch (InsufficientFundsException e) {
			redir.addFlashAttribute("noFunds", "You don't have sufficient funds to place that order!");
			return "redirect:/order";
		} catch (RecursiveTradeException e) {
			redir.addFlashAttribute("recursive", "You can't trade a currency for itself!");
			return "redirect:/order";
		} catch (InvalidDateException e) {
			redir.addFlashAttribute("invalidDate", "You can't choose an expiry date after the scheduled trade date!");
			return "redirect:/order";
		}
	}
	
}

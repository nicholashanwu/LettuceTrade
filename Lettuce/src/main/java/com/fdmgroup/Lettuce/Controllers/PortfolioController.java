package com.fdmgroup.Lettuce.Controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import com.fdmgroup.Lettuce.Models.Portfolio;
import com.fdmgroup.Lettuce.Models.User;
import com.fdmgroup.Lettuce.Repo.CurrencyRepo;
import com.fdmgroup.Lettuce.Repo.HeldCurrencyRepo;
import com.fdmgroup.Lettuce.Repo.PortfolioRepo;
import com.fdmgroup.Lettuce.Service.CurrencyServiceImpl;
import com.fdmgroup.Lettuce.Service.OrderServiceImpl;
import com.fdmgroup.Lettuce.Service.PortfolioServiceImpl;
import com.fdmgroup.Lettuce.Service.UserServiceImpl;
import com.fdmgroup.Lettuce.rates.ExchangeRate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Controller
@SessionAttributes({"user"})
public class PortfolioController {
	@Autowired
	private UserServiceImpl usi = new UserServiceImpl();
	@Autowired
	private CurrencyServiceImpl csi = new CurrencyServiceImpl();
	@Autowired
	private PortfolioServiceImpl psi = new PortfolioServiceImpl();
	@Autowired
	private OrderServiceImpl osi = new OrderServiceImpl();
	@Autowired
	private PortfolioRepo pr;
	@Autowired
	private HeldCurrencyRepo hcr;
	@Autowired
	private CurrencyRepo cr;
	
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
		
		User user = (User) request.getSession().getAttribute("user");
		
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
//	
//	@RequestMapping("/addPortfolioHandler")
//	public String addPortfolioHandler(@RequestParam int userId) {
//		
//		// suggested to applied at user registeration
//		
//		User user = usi.getUserById(userId);
//		Portfolio portfolio = new Portfolio(user);
//		psi.addPortfolio(portfolio);
//		
//		dbLogger.info("Added "+portfolio+" into database successfully");
//		return "order";     // may require revise
//	}
//	
//	@RequestMapping("/deletePortfolioHandler")
//	public String deletePortfolioHandler(@RequestParam int portfolio_id) {
//		
//		// user may want to delete its portfolio
//		
//		psi.deletePortfolioById(portfolio_id);
//		
//		dbLogger.info("Deleted "+psi.getPortfolioById(portfolio_id)+" from database successfully");
//		return "userDashboard";
//	}
//	
//	@RequestMapping("/orderHandler")
//	public String orderHandler(Currency currency, @RequestParam double quantity, Portfolio portfolio, @RequestParam String flag) {
//		actLogger.info("Dealing with order");
//		
//		if(flag.equals("increase")) {
//			psi.increaseCurrency(currency, quantity, portfolio.getPortfolioId());
//		}else if(flag.equals("decrease")) {
//			try {
//				psi.decreaseCurrency(currency, quantity, portfolio.getPortfolioId());
//			} catch (InsufficientFundsException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return "order";
//	}
	
	@RequestMapping("/addfund")
	public String addFundPage() {
		return "addfund";
	}

	@RequestMapping(value = "/fundHandler",params = "top-up",method=RequestMethod.POST)
	public String addFundHandler(@RequestParam double amount, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		int pid = user.getPortfolio().getPortfolioId();
		Currency aud = cr.getById("AUD");
		psi.increaseCurrency(aud, amount, pid);
		return "redirect:/profile";
	}

	@RequestMapping(value = "/fundHandler", params = "withdraw", method = RequestMethod.POST)
	public String withdrawFundHandler(@RequestParam double amount, HttpServletRequest request, RedirectAttributes redir) throws InsufficientFundsException {
		User user = (User) request.getSession().getAttribute("user");
		int pid = user.getPortfolio().getPortfolioId();
		Currency aud = cr.getById("AUD");
		try {
			psi.decreaseCurrency(aud, amount, pid);
			return "redirect:/profile";
		} catch(InsufficientFundsException e) {
			redir.addFlashAttribute("noFunds", "You do not have sufficient funds to withdraw!");
			return "redirect:/addfund";
		}
	}
	
//	@RequestMapping("/order")
//	public String orderPage(Model model) {
//		model.addAttribute("newOrder", new Order());
//		model.addAttribute("currencies", csi.getAllCurrency());
//		return "order";
//	}
	
	@RequestMapping("/history")
	public String historyPage(Model model, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		List<Order> orders = osi.getAllOrdersForUser(user);
		orders.sort(Comparator.comparingInt(Order::getOrderId).reversed());
		model.addAttribute("orders", orders);
		model.addAttribute("now", LocalDate.now());
		try {
			model.addAttribute("rates", getFilteredRates("USD"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "history";
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
	
	@RequestMapping("/cancelorder")
	public String cancelHandler(@RequestParam int orderId) {
		osi.cancelOrder(osi.getOrderById(orderId));
		return "redirect:/history";
	}
	
	@RequestMapping("/outstandingOrder")
	public String outOrderPage(Model model, HttpServletRequest request) throws IOException {
	User user = (User) request.getSession().getAttribute("user");
	HashMap<String, Double> quantRates = new HashMap<String, Double>();
	List<String> ordersSameCurrencies = new ArrayList<>();

	for (Order order : osi.getAllOrdersNotUser(user, OrderStatus.ACTIVE, OrderStatus.PARTIALLY_COMPLETE)) {
		Double t = ExchangeRate.getRateForPair(order.getBaseCurrency().toString(), order.getTargetCurrency().toString());
		String r = order.getBaseCurrency().toString()+ " to " + order.getTargetCurrency().toString();
		Double quant = quantRates.get(r + " at " + t);
		
		if (quant == null) {
			quantRates.put(r + " at " +  t, order.getQuantity());
		} else {
			quantRates.put(r + " at " +  t, quant + order.getQuantity());
		}
		if (!ordersSameCurrencies.contains(order.getBaseCurrency().toString() + " to " + order.getTargetCurrency().toString()+ " at " + t)){
			ordersSameCurrencies.add(0, (order.getBaseCurrency().toString() + " to " + order.getTargetCurrency().toString()+ " at " + t));
		}
	
	}
	System.out.println(quantRates);
	model.addAttribute("outRates", quantRates);

	model.addAttribute("newOrder", new Order());
	//model.addAttribute("outOrder", osi.getAllOrdersNotUser(user)); //null, OrderStatus.))
	return "outstandingOrder";
		
	}
	@RequestMapping("/outOrderHandler")
	public String outOrderHandler(Order newOrder, HttpServletRequest request, RedirectAttributes redir) {
		User user = (User) request.getSession().getAttribute("user");
		System.out.println(newOrder.getDetails());
		System.out.println(newOrder.getInitialQuantity());

		return "redirect:/outstandingOrder";
	}

	

	
}

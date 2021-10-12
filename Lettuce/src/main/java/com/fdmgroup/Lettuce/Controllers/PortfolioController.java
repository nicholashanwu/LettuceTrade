package com.fdmgroup.Lettuce.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.HeldCurrency;
import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.OrderStatus;
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
	
	
//	@RequestMapping("/order")
//	public String orderPage(Model model, HttpServletRequest request) {    // equivalent to get portfolio_id
//		actLogger.info("Landed in Order page");
//		
//		User user = (User) request.getSession().getAttribute("user");
//		
//		
//		System.out.println(user);
//		
//		
//		List<HeldCurrency> heldCurrencies = hcr.findByPortfolio(user.getPortfolio());
//		
////		List<Currency> currencies = new ArrayList<Currency>();
//		
//		HashMap<Currency, Double> currenciesAndQuantities = new HashMap<>();
//		
//		
//		// users can only sell currency that they have a positive amount of
//		for (HeldCurrency hc : heldCurrencies) {
//			if(hc.getQuantity() > 0) {
//				currenciesAndQuantities.put(hc.getCurrency(), hc.getQuantity());
//			}
//		}
//		
////		System.out.println(currencies);
//		
//		//get list of currencies and pass them to model
//		
//		Map<String, Double> rates;
//		
//		try {
//			rates = ExchangeRate.getAllRates("USD");
//			model.addAttribute("rates", rates);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("could not get rates");
//		}
//		
//	
//		model.addAttribute("portfolio", currenciesAndQuantities);
//		model.addAttribute("currency", new Currency());
//		model.addAttribute("quantity", 0.0);
//		
//		return "ordertemp";
//	}
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
	
	@RequestMapping("/order")
	public String orderPage(Model model) {
		model.addAttribute("newOrder", new Order());
		model.addAttribute("currencies", csi.getAllCurrency());
		return "order";
	}
	
	@RequestMapping("/history")
	public String historyPage(Model model, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		model.addAttribute("orders", osi.getAllOrdersForUser(user));
		return "history";
	}
	
	@RequestMapping("/orderHandler")
	public String orderHandler(Order order, HttpServletRequest request, RedirectAttributes redir) {
		User user = (User) request.getSession().getAttribute("user");
		order.setUser(user);
		order.setQuantity(order.getInitialQuantity());
		order.setOrderStatus(OrderStatus.PENDING);
		try {
			osi.addOrder(order);
			return "redirect:/dashboard";
		} catch (InsufficientFundsException e) {
			redir.addFlashAttribute("noFunds", "You don't have sufficient funds to place that order!");
			return "redirect:/order";
		}
	}
	
}

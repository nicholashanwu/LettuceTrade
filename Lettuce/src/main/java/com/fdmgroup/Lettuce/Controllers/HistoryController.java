package com.fdmgroup.Lettuce.Controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.User;
import com.fdmgroup.Lettuce.Service.CurrencyServiceImpl;
import com.fdmgroup.Lettuce.Service.OrderServiceImpl;
import com.fdmgroup.Lettuce.rates.ExchangeRate;

@Controller
@SessionAttributes({"user"})
public class HistoryController {
	@Autowired
	private CurrencyServiceImpl csi = new CurrencyServiceImpl();
	@Autowired
	private OrderServiceImpl osi = new OrderServiceImpl();
	
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
	
	@RequestMapping("/cancelorder")
	public String cancelHandler(@RequestParam int orderId) {
		osi.cancelOrder(osi.getOrderById(orderId));
		return "redirect:/history";
	}

}

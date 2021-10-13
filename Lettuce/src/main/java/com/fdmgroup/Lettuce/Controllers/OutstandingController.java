package com.fdmgroup.Lettuce.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.OrderStatus;
import com.fdmgroup.Lettuce.Models.User;
import com.fdmgroup.Lettuce.Service.OrderServiceImpl;
import com.fdmgroup.Lettuce.rates.ExchangeRate;

@Controller
@SessionAttributes({"user"})
public class OutstandingController {
	@Autowired
	private OrderServiceImpl osi = new OrderServiceImpl();
	
	public static final Logger actLogger=LogManager.getLogger("ActLogging");
	public static final Logger dbLogger=LogManager.getLogger("DBLogging");
	
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

package com.fdmgroup.Lettuce.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdmgroup.Lettuce.Exceptions.InsufficientFundsException;
import com.fdmgroup.Lettuce.Exceptions.InvalidDateException;
import com.fdmgroup.Lettuce.Exceptions.RecursiveTradeException;
import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.OrderStatus;
import com.fdmgroup.Lettuce.Models.OrderType;
import com.fdmgroup.Lettuce.Models.User;
import com.fdmgroup.Lettuce.Service.CurrencyServiceImpl;
import com.fdmgroup.Lettuce.Service.OrderServiceImpl;
import com.fdmgroup.Lettuce.Service.TransactionServiceImpl;
import com.fdmgroup.Lettuce.rates.ExchangeRate;

@Controller
@SessionAttributes({ "user" })
public class OutstandingController {
	@Autowired
	private OrderServiceImpl osi = new OrderServiceImpl();
	@Autowired
	private CurrencyServiceImpl csi = new CurrencyServiceImpl();
	@Autowired
	private TransactionServiceImpl tsi = new TransactionServiceImpl();

	public static final Logger actLogger = LogManager.getLogger("ActLogging");
	public static final Logger dbLogger = LogManager.getLogger("DBLogging");

	@RequestMapping("/outstandingOrder")
	public String outOrderPage(Model model, HttpServletRequest request) throws IOException {
		
		osi.expireAll();
		tsi.resolveAllPending();
		
		User user = (User) request.getSession().getAttribute("user");
		HashMap<String, Double> quantRates = new HashMap<String, Double>();

		for (Order order : osi.getAllOrdersNotUser(user, OrderStatus.ACTIVE, OrderStatus.PARTIALLY_COMPLETE)) {
			Double t = ExchangeRate.getRateForPair(order.getBaseCurrency().toString(),
					order.getTargetCurrency().toString());
			String r = order.getBaseCurrency().toString() + " to " + order.getTargetCurrency().toString();
			Double quant = quantRates.get(r + " at " + t);

			if (quant == null) {
				quantRates.put(r + " at " + t, order.getQuantity());
			} else {
				quantRates.put(r + " at " + t, quant + order.getQuantity());
			}

		}
		model.addAttribute("outRates", quantRates);
		model.addAttribute("newOrder", new Order());
		// model.addAttribute("outOrder", osi.getAllOrdersNotUser(user)); //null,
		// OrderStatus.))
		return "outstandingOrder";

	}

	@RequestMapping("/outOrderHandler")
	public String outOrderHandler(Order newOrder, HttpServletRequest request, RedirectAttributes redir)
			throws IOException, RecursiveTradeException, InvalidDateException {
		User user = (User) request.getSession().getAttribute("user");
		newOrder.setUser(user);
		Scanner sc = new Scanner(newOrder.getDetails());
		List<String> word = new ArrayList<>();
		while (sc.hasNext()) {
			word.add(sc.next());
		}
		newOrder.setBaseCurrency(csi.getCurrencyById(word.get(2)));
		newOrder.setTargetCurrency(csi.getCurrencyById(word.get(0)));
		Double convertedQuant = newOrder.getInitialQuantity() * ExchangeRate
				.getRateForPair(newOrder.getTargetCurrency().toString(), newOrder.getBaseCurrency().toString());
		newOrder.setQuantity(convertedQuant);
		newOrder.setOrderStatus(OrderStatus.ACTIVE);
		newOrder.setOrderType(OrderType.SPOT);

		try {
			osi.addOrder(newOrder);
			return "redirect:/dashboard";
		} catch (InsufficientFundsException e) {
			redir.addFlashAttribute("noFunds", "You don't have sufficient funds to place that order!");
			return "redirect:/outstandingOrder";
		}

	}

}

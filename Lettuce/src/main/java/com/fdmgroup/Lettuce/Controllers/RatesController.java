package com.fdmgroup.Lettuce.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fdmgroup.Lettuce.rates.ExchangeRate;

@Controller
@SessionAttributes({"user"})
public class RatesController {
	public static final Logger actLogger=LogManager.getLogger("ActLogging");

	@RequestMapping("/rates")
	public String ratesPage(Model model) {
		// Showing all rates for any currency pairs
		
		actLogger.info("Landed in rates page");
		
		List<Double> ratelist = new ArrayList<>();
		
		// "AUD", "USD", "CAD", "GBP", "NZD"
		
		try {
			ratelist.add(ExchangeRate.getRateForPair("AUD", "USD"));
			ratelist.add(ExchangeRate.getRateForPair("AUD", "CAD"));
			ratelist.add(ExchangeRate.getRateForPair("AUD", "GBP"));
			ratelist.add(ExchangeRate.getRateForPair("AUD", "NZD"));
			ratelist.add(ExchangeRate.getRateForPair("USD", "CAD"));
			ratelist.add(ExchangeRate.getRateForPair("USD", "GBP"));
			ratelist.add(ExchangeRate.getRateForPair("USD", "NZD"));
			ratelist.add(ExchangeRate.getRateForPair("CAD", "GBP"));
			ratelist.add(ExchangeRate.getRateForPair("CAD", "NZD"));
			ratelist.add(ExchangeRate.getRateForPair("GBP", "NZD"));
			
			actLogger.info("Showing rates");
			model.addAttribute("ratelist", ratelist);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return "rates";
	}
}

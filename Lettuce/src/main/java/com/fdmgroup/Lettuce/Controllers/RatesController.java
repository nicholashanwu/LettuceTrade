package com.fdmgroup.Lettuce.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Service.CurrencyServiceImpl;
import com.fdmgroup.Lettuce.rates.ExchangeRate;

@Controller
@SessionAttributes({"user"})
public class RatesController {
	@Autowired
	private CurrencyServiceImpl csi = new CurrencyServiceImpl();
	
	public static final Logger actLogger=LogManager.getLogger("ActLogging");

	@RequestMapping("/rates")
	public String ratesPage(Model model) {
		// Showing all rates for any currency pairs
		
		actLogger.info("Landed in rates page");
		
		// "AUD", "USD", "CAD", "GBP", "NZD", "EUR", "JPY"
		
		try {
			Map<String, Double> ratesAsStrings = ExchangeRate.getAllRates("USD");
			Map<Currency, Double> rates = new HashMap<>();
			for (Map.Entry<String, Double> rate : ratesAsStrings.entrySet()) {
				try {
					Currency currency = csi.getCurrencyById(rate.getKey());
					rates.put(currency, rate.getValue());
				} catch (EntityNotFoundException e) {
					// API has given us a currency that we're not tracking in our database.
					// Don't add this one to the map. Do nothing.
				}
			}
			System.out.println(rates);
			
			actLogger.info("Showing rates");
			model.addAttribute("rates", rates);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return "rates";
	}
}

package com.fdmgroup.Lettuce.rates;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class ExchangeRate {

	private static final String baseUrl = "https://v6.exchangerate-api.com/v6/7e45aa311d560c1d005ccfa4/";
	
	private static final String popularCurrencies = "USD|EUR|JPY|GBP|AUD";

	/**
	 * Contacts external API and gets the conversion rate between two currencies.
	 * 
	 * @param baseCurrency   the currency you are spending, as a 3-letter string.
	 * @param targetCurrency the currency you are buying, as a 3-letter string.
	 * @return a double for how much of the target currency you can buy with 1 unit
	 *         of the base currency.
	 * @throws IOException if there is a connection failure, or if an invalid
	 *                     currency code is passed.
	 */
	public static double getRateForPair(String baseCurrency, String targetCurrency) throws IOException {
		// Build URL
		String urlStr = baseUrl + "pair/" + baseCurrency + "/" + targetCurrency;

		// Make Request
		URL url = new URL(urlStr);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();

		// Get result as JSON
		InputStreamReader resultStream = new InputStreamReader((InputStream) request.getContent());
		JsonObject resultJson = JsonParser.parseReader(resultStream).getAsJsonObject();

		// Retrieve data
		double rate = resultJson.get("conversion_rate").getAsDouble();
		return rate;
	}

	/**
	 * Contacts external API and gets the values of all world currencies compared to
	 * a base currency.
	 * 
	 * @param baseCurrency the currency you are spending, as a 3-letter string.
	 * @return a map of all the currency names and their values.
	 * @throws IOException if there is a connection failure, or if an invalid
	 *                     currency code is passed.
	 */
	public static Map<String, Double> getAllRates(String baseCurrency) throws IOException {
		// Build URL
		String urlStr = baseUrl + "latest/" + baseCurrency;

		// Make Request
		URL url = new URL(urlStr);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();

		// Get result as JSON
		InputStreamReader resultStream = new InputStreamReader((InputStream) request.getContent());
		JsonObject resultJson = JsonParser.parseReader(resultStream).getAsJsonObject();

		// Retrieve data
		TreeMap<String, Double> rates = new Gson().fromJson(resultJson.get("conversion_rates"),
				new TypeToken<TreeMap<String, Double>>() {
				}.getType());
		return rates;
	}

	/**
	 * Contacts external API and gets the values of a hardcoded selection of popular
	 * currencies compared to a base currency.
	 * 
	 * @param baseCurrency the currency you are spending, as a 3-letter string.
	 * @return a map of all the currency names and their values.
	 * @throws IOException if there is a connection failure, or if an invalid
	 *                     currency code is passed.
	 */
	public static Map<String, Double> getPopularRates(String baseCurrency) throws IOException {
		Map<String, Double> rates = getAllRates(baseCurrency);
		Map<String, Double> filteredRates = new TreeMap<>();
		for (Map.Entry<String, Double> rate : rates.entrySet()) {
			if (rate.getKey().matches(popularCurrencies)) {
				filteredRates.put(rate.getKey(), rate.getValue());
			}
		}
		return filteredRates;
	}
}

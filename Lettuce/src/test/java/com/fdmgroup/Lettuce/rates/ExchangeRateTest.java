package com.fdmgroup.Lettuce.rates;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class ExchangeRateTest {
	
	@Test
	public void test_getPair_validInput() {
		try {
			double rate = ExchangeRate.getRateForPair("USD", "EUR");
			
			assertThat(rate).isBetween(0.8, 0.9); // This is not guaranteed true, but the exchange rate has been within this range for more than a year
			
			System.out.println(rate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_getPair_invalidInput() {
		assertThrows(IOException.class, ()->{ExchangeRate.getRateForPair("nonsense", "gibberish");});
	}
	
	@Test
	public void test_getAll_validInput() {
		try {
			Map<String, Double> rates = ExchangeRate.getAllRates("USD");
			
			assertThat(rates).isNotEmpty();
			
			double rate = rates.get("EUR");
			assertThat(rate).isBetween(0.8, 0.9);
			
			System.out.println(rates);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

package com.fdmgroup.Lettuce;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fdmgroup.Lettuce.rates.ExchangeRate;

public class ExchangeRateTest {

	@Test
	public void test_getPair_validInput() throws IOException {
		double rate = ExchangeRate.getRateForPair("USD", "EUR");

		assertThat(rate).isBetween(0.8, 0.9); // This is not guaranteed true, but the exchange rate has been within this
												// range for more than a year

		System.out.println(rate);
	}

	@Test
	public void test_getPair_invalidInput() {
		assertThrows(IOException.class, () -> {
			ExchangeRate.getRateForPair("nonsense", "gibberish");
		});
	}

	@Test
	public void test_getAll_validInput() throws IOException {
		Map<String, Double> rates = ExchangeRate.getAllRates("USD");

		assertThat(rates).isNotEmpty();

		double rate = rates.get("EUR");
		assertThat(rate).isBetween(0.8, 0.9);

		System.out.println(rates);
	}

	@Test
	public void test_getPopular() throws IOException {
		Map<String, Double> rates = ExchangeRate.getPopularRates("USD");

		assertThat(rates).hasSize(5);
		
		System.out.println(rates);
	}
}

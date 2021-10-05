package com.fdmgroup.Lettuce;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Repo.CurrencyRepo;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CurrencyDBTesting {
	
	@Autowired
	private TestEntityManager em;
	@Autowired
	CurrencyRepo cr;
	
	@Test
	public void should_find_no_currency_if_repository_is_empty() {
		Iterable<Currency> currencies = cr.findAll();
		assertThat(currencies).isEmpty();
	}
	
	@Test
	public void should_store_a_currency() {
		Currency currency = cr.save(new Currency("USD"));
		assertThat(currency).hasFieldOrPropertyWithValue("currency_name", "USD");
	}
	
	@Test
	public void should_find_all_currency() {
		Currency currency1 = new Currency("USD");
		Currency currency2 = new Currency("AUD");
		Currency currency3 = new Currency("HKD");
		
		em.persist(currency1);
		em.persist(currency2);
		em.persist(currency3);
		
		Iterable<Currency> currencies = cr.findAll();
		assertThat(currencies).hasSize(3).contains(currency1,currency2,currency3);
		
	}
	
	@Test
	public void should_find_currency_by_id() {
		Currency currency1 = new Currency("USD");
		Currency currency2 = new Currency("AUD");
		
		em.persist(currency1);
		em.persist(currency2);
	
		
		Currency findCurrency = cr.findById(currency2.getCurrency_id()).get();
		assertThat(findCurrency).isEqualTo(currency2);
	}
	
	@Test
	public void should_update_Currency_by_id() {
		Currency currency1 = new Currency("USD");
		Currency currency2 = new Currency("AUD");
		
		em.persist(currency1);
		em.persist(currency2);
	
		Currency updateCurrency = new Currency("USD1");
		
		Currency currency = cr.findById(currency2.getCurrency_id()).get();
		currency.setCurrency_name(updateCurrency.getCurrency_name());
		cr.save(currency);
		
		
		Currency checkCurrency = cr.findById(currency2.getCurrency_id()).get();
		assertThat(checkCurrency.getCurrency_id()).isEqualTo(currency2.getCurrency_id());
		assertThat(checkCurrency.getCurrency_name()).isEqualTo(updateCurrency.getCurrency_name());
		
	}

	@Test
	public void delete_currency_by_id() {
		Currency currency1 = new Currency("USD");
		Currency currency2 = new Currency("AUD");
		
		em.persist(currency1);
		em.persist(currency2);
		
		cr.deleteById(currency2.getCurrency_id());
		
		Iterable<Currency> currencies = cr.findAll();
		assertThat(currencies).hasSize(1).contains(currency1);
		
	}
}

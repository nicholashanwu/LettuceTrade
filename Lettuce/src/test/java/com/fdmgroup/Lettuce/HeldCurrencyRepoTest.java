package com.fdmgroup.Lettuce;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.persistence.EntityExistsException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.HeldCurrency;
import com.fdmgroup.Lettuce.Models.Portfolio;
import com.fdmgroup.Lettuce.Models.User;
import com.fdmgroup.Lettuce.Repo.HeldCurrencyRepo;

@RunWith(SpringRunner.class)
@DataJpaTest
public class HeldCurrencyRepoTest {
	
	@Autowired
	private TestEntityManager em;
	@Autowired
	private HeldCurrencyRepo hcr;
	
	@Test
	public void should_find_none_if_repo_is_empty() {
		Iterable<HeldCurrency> heldCurrencies = hcr.findAll();
		assertThat(heldCurrencies).isEmpty();
	}
	
	@Test
	public void should_store_a_record() {
		Portfolio p = new Portfolio();
		Currency c = new Currency("USD");
		em.persist(p);
		em.persist(c);
		HeldCurrency heldCurrency = hcr.save(new HeldCurrency(p, c));
		assertThat(heldCurrency).hasFieldOrPropertyWithValue("portfolio", p);
		assertThat(heldCurrency).hasFieldOrPropertyWithValue("currency", c);

	}
	
	@Test
	public void should_find_all() {
		Portfolio p1 = new Portfolio();
		Portfolio p2 = new Portfolio();
		Portfolio p3 = new Portfolio();
		Currency c = new Currency("USD");
		
		HeldCurrency hc1 = new HeldCurrency(p1, c);
		HeldCurrency hc2 = new HeldCurrency(p2, c);
		HeldCurrency hc3 = new HeldCurrency(p3, c);
		
		em.persist(hc1);
		em.persist(hc2);
		em.persist(hc3);
		
		Iterable<HeldCurrency> heldCurrencies = hcr.findAll();
		assertThat(heldCurrencies).hasSize(3).contains(hc1, hc2, hc3);
	}
	
	@Test
	public void should_find_by_portfolio() {
		Portfolio p1 = new Portfolio();
		Portfolio p2 = new Portfolio();
		Portfolio p3 = new Portfolio();
		Currency c = new Currency("USD");
		
		HeldCurrency hc1 = new HeldCurrency(p1, c);
		HeldCurrency hc2 = new HeldCurrency(p2, c);
		HeldCurrency hc3 = new HeldCurrency(p3, c);
		
		em.persist(hc1);
		em.persist(hc2);
		em.persist(hc3);
		
		Iterable<HeldCurrency> heldCurrencies = hcr.findByPortfolio(p2);
		assertThat(heldCurrencies).hasSize(1).contains(hc2);
	}
	
	@Test
	public void should_find_by_currency() {
		Portfolio p1 = new Portfolio();
		Portfolio p2 = new Portfolio();
		Portfolio p3 = new Portfolio();
		Currency c1 = new Currency("USD");
		Currency c2 = new Currency("EUR");
		
		HeldCurrency hc1 = new HeldCurrency(p1, c1);
		HeldCurrency hc2 = new HeldCurrency(p2, c2);
		HeldCurrency hc3 = new HeldCurrency(p3, c1);
		
		em.persist(hc1);
		em.persist(hc2);
		em.persist(hc3);
		
		Iterable<HeldCurrency> heldCurrencies = hcr.findByCurrency(c1);
		assertThat(heldCurrencies).hasSize(2).contains(hc1, hc3);
	}
	
	@Test
	public void should_find_unique_value() {
		Portfolio p1 = new Portfolio();
		Portfolio p2 = new Portfolio();
		Portfolio p3 = new Portfolio();
		Currency c1 = new Currency("USD");
		Currency c2 = new Currency("EUR");
		
		HeldCurrency hc1 = new HeldCurrency(p1, c1);
		HeldCurrency hc2 = new HeldCurrency(p2, c2);
		HeldCurrency hc3 = new HeldCurrency(p3, c1);
		
		em.persist(hc1);
		em.persist(hc2);
		em.persist(hc3);
		
		HeldCurrency heldCurrency = hcr.getByPortfolioAndCurrency(p1, c1);
		assertThat(heldCurrency).isEqualTo(hc1);
	}
	
	@Test
	public void cannot_insert_duplicate_value() {
		Portfolio p1 = new Portfolio();
		Portfolio p2 = new Portfolio();
		Portfolio p3 = new Portfolio();
		Currency c1 = new Currency("USD");
		Currency c2 = new Currency("EUR");
		
		HeldCurrency hc1 = new HeldCurrency(p1, c1);
		HeldCurrency hc2 = new HeldCurrency(p2, c2);
		HeldCurrency hc3 = new HeldCurrency(p1, c1);
		
		em.persist(hc1);
		em.persist(hc2);
		assertThrows(EntityExistsException.class, ()->{em.persist(hc3);});
		
	}

}

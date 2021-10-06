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
		User u = new User();
		Currency c = new Currency("USD");
		em.persist(u);
		em.persist(c);
		HeldCurrency heldCurrency = hcr.save(new HeldCurrency(u, c));
		assertThat(heldCurrency).hasFieldOrPropertyWithValue("user", u);
		assertThat(heldCurrency).hasFieldOrPropertyWithValue("currency", c);

	}
	
	@Test
	public void should_find_all() {
		User u1 = new User();
		User u2 = new User();
		User u3 = new User();
		Currency c = new Currency("USD");
		
		HeldCurrency hc1 = new HeldCurrency(u1, c);
		HeldCurrency hc2 = new HeldCurrency(u2, c);
		HeldCurrency hc3 = new HeldCurrency(u3, c);
		
		em.persist(hc1);
		em.persist(hc2);
		em.persist(hc3);
		
		Iterable<HeldCurrency> heldCurrencies = hcr.findAll();
		assertThat(heldCurrencies).hasSize(3).contains(hc1, hc2, hc3);
	}
	
	@Test
	public void should_find_by_user() {
		User u1 = new User();
		User u2 = new User();
		User u3 = new User();
		Currency c = new Currency("USD");
		
		HeldCurrency hc1 = new HeldCurrency(u1, c);
		HeldCurrency hc2 = new HeldCurrency(u2, c);
		HeldCurrency hc3 = new HeldCurrency(u3, c);
		
		em.persist(hc1);
		em.persist(hc2);
		em.persist(hc3);
		
		Iterable<HeldCurrency> heldCurrencies = hcr.findByUser(u2);
		assertThat(heldCurrencies).hasSize(1).contains(hc2);
	}
	
	@Test
	public void should_find_by_currency() {
		User u1 = new User();
		User u2 = new User();
		User u3 = new User();
		Currency c1 = new Currency("USD");
		Currency c2 = new Currency("EUR");
		
		HeldCurrency hc1 = new HeldCurrency(u1, c1);
		HeldCurrency hc2 = new HeldCurrency(u2, c2);
		HeldCurrency hc3 = new HeldCurrency(u3, c1);
		
		em.persist(hc1);
		em.persist(hc2);
		em.persist(hc3);
		
		Iterable<HeldCurrency> heldCurrencies = hcr.findByCurrency(c1);
		assertThat(heldCurrencies).hasSize(2).contains(hc1, hc3);
	}
	
	@Test
	public void should_find_unique_value() {
		User u1 = new User();
		User u2 = new User();
		User u3 = new User();
		Currency c1 = new Currency("USD");
		Currency c2 = new Currency("EUR");
		
		HeldCurrency hc1 = new HeldCurrency(u1, c1);
		HeldCurrency hc2 = new HeldCurrency(u2, c2);
		HeldCurrency hc3 = new HeldCurrency(u3, c1);
		
		em.persist(hc1);
		em.persist(hc2);
		em.persist(hc3);
		
		HeldCurrency heldCurrency = hcr.getByUserAndCurrency(u1, c1);
		assertThat(heldCurrency).isEqualTo(hc1);
	}
	
	@Test
	public void cannot_insert_duplicate_value() {
		User u1 = new User();
		User u2 = new User();
		User u3 = new User();
		Currency c1 = new Currency("USD");
		Currency c2 = new Currency("EUR");
		
		HeldCurrency hc1 = new HeldCurrency(u1, c1);
		HeldCurrency hc2 = new HeldCurrency(u2, c2);
		HeldCurrency hc3 = new HeldCurrency(u1, c1);
		
		em.persist(hc1);
		em.persist(hc2);
		assertThrows(EntityExistsException.class, ()->{em.persist(hc3);});
		
	}

}

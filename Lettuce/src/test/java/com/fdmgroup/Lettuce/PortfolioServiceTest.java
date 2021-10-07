package com.fdmgroup.Lettuce;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.fdmgroup.Lettuce.Exceptions.InsufficientFundsException;
import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.HeldCurrency;
import com.fdmgroup.Lettuce.Models.Portfolio;
import com.fdmgroup.Lettuce.Repo.PortfolioRepo;
import com.fdmgroup.Lettuce.Service.PortfolioServiceImpl;

@RunWith(SpringRunner.class)
public class PortfolioServiceTest {
	
	@Mock
	PortfolioRepo repo;
	
	@InjectMocks
	PortfolioServiceImpl service;
	
	@Test
	public void should_add_and_get_portfolio() {
		// Arrange
		Portfolio p = new Portfolio();
		service.addPortfolio(p);
		
		// Mock the repo's return value
		when(repo.getById(p.getPortfolioId())).thenReturn(p);
		
		// Act
		Portfolio got = service.getPortfolioById(p.getPortfolioId());
		
		// Assert
		assertThat(got).isEqualTo(p);
	}
	
	@Test
	public void increaseCurrency_should_add_to_existing_record() {
		// Arrange
		Currency c = new Currency();
		Portfolio p = new Portfolio();
		HeldCurrency hc = new HeldCurrency(p, c);
		hc.setQuantity(20.0);
		List<HeldCurrency> held = new ArrayList<>();
		held.add(hc);
		p.setHeldCurrencies(held);
		
		// Mock the repo's return value
		when(repo.getById(p.getPortfolioId())).thenReturn(p);
		
		// Act
		service.increaseCurrency(c, 50.0, p.getPortfolioId());
		
		// Assert
		assertThat(p.getHeldCurrencies().get(0).getPortfolio()).isEqualTo(p);
		assertThat(p.getHeldCurrencies().get(0).getCurrency()).isEqualTo(c);
		assertThat(p.getHeldCurrencies().get(0).getQuantity()).isCloseTo(70.0, within(0.1));
	}
	
	@Test
	public void increaseCurrency_should_create_new_record() {
		// Arrange
		Currency c = new Currency();
		Portfolio p = new Portfolio();
		
		// Mock the repo's return value
		when(repo.getById(p.getPortfolioId())).thenReturn(p);
		
		// Act
		service.increaseCurrency(c, 50.0, p.getPortfolioId());
		
		// Assert
		assertThat(p.getHeldCurrencies().get(0).getPortfolio()).isEqualTo(p);
		assertThat(p.getHeldCurrencies().get(0).getCurrency()).isEqualTo(c);
		assertThat(p.getHeldCurrencies().get(0).getQuantity()).isCloseTo(50.0, within(0.1));
	}
	
	@Test
	public void decreaseCurrency_should_subtract_from_existing_record() throws InsufficientFundsException {
		// Arrange
		Currency c = new Currency();
		Portfolio p = new Portfolio();
		HeldCurrency hc = new HeldCurrency(p, c);
		hc.setQuantity(20.0);
		List<HeldCurrency> held = new ArrayList<>();
		held.add(hc);
		p.setHeldCurrencies(held);
		
		// Mock the repo's return value
		when(repo.getById(p.getPortfolioId())).thenReturn(p);
		
		// Act
		service.decreaseCurrency(c, 10.0, p.getPortfolioId());
		
		// Assert
		assertThat(p.getHeldCurrencies().get(0).getPortfolio()).isEqualTo(p);
		assertThat(p.getHeldCurrencies().get(0).getCurrency()).isEqualTo(c);
		assertThat(p.getHeldCurrencies().get(0).getQuantity()).isCloseTo(10.0, within(0.1));
	}
	
	@Test(expected = InsufficientFundsException.class)
	public void decreaseCurrency_should_fail_if_insufficient_money() throws InsufficientFundsException {
		// Arrange
		Currency c = new Currency();
		Portfolio p = new Portfolio();
		HeldCurrency hc = new HeldCurrency(p, c);
		hc.setQuantity(20.0);
		List<HeldCurrency> held = new ArrayList<>();
		held.add(hc);
		p.setHeldCurrencies(held);
		
		// Mock the repo's return value
		when(repo.getById(p.getPortfolioId())).thenReturn(p);
		
		// Act
		service.decreaseCurrency(c, 50.0, p.getPortfolioId());
		
		// Assert - handled by the 'expected' annotation above
	}

	@Test(expected = InsufficientFundsException.class)
	public void decreaseCurrency_should_fail_if_currency_not_found() throws InsufficientFundsException {
		// Arrange
		Currency c = new Currency();
		Portfolio p = new Portfolio();
		
		// Mock the repo's return value
		when(repo.getById(p.getPortfolioId())).thenReturn(p);
		
		// Act
		service.decreaseCurrency(c, 50.0, p.getPortfolioId());
		
		// Assert - handled by the 'expected' annotation above
	}

}

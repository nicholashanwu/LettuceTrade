package com.fdmgroup.Lettuce.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.Portfolio;
import com.fdmgroup.Lettuce.Repo.PortfolioRepo;

@Service
public class PortfolioServiceImpl implements iPortfolio {
	@Autowired
	private PortfolioRepo portfolioRepo;

	@Override
	public List<Portfolio> getAllPortfolios() {
		return portfolioRepo.findAll();
	}

	@Override
	public Portfolio getPortfolioById(int portfolio_id) {
		return portfolioRepo.getById(portfolio_id);
	}

	@Override
	public void addPortfolio(Portfolio portfolio) {
		portfolioRepo.save(portfolio);
	}

	@Override
	public void deletePortfolio(Portfolio portfolio) {
		portfolioRepo.delete(portfolio);
	}

	@Override
	public void deletePortfolioById(int portfolio_id) {
		portfolioRepo.deleteById(portfolio_id);
	}

	@Override
	public void deleteAllPortfolios() {
		portfolioRepo.deleteAll();
	}

	@Override
	public void increaseCurrency(Currency currency, Double quantity, int portfolio_id) {
		Portfolio portfolio = portfolioRepo.getById(portfolio_id);
		Map<Currency, Double> currencies_held = portfolio.getCurrencies_held();
		currencies_held.put(currency, currencies_held.get(currency)+quantity);
		portfolio.setCurrencies_held(currencies_held);
	}

	@Override
	public void decreaseCurrency(Currency currency, Double quantity, int portfolio_id) {
		Portfolio portfolio = portfolioRepo.getById(portfolio_id);
		Map<Currency, Double> currencies_held = portfolio.getCurrencies_held();
		currencies_held.put(currency, currencies_held.get(currency)-quantity);
		portfolio.setCurrencies_held(currencies_held);
	}

	
}

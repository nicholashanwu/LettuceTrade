package com.fdmgroup.Lettuce.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.Lettuce.Exceptions.InsufficientFundsException;
import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.HeldCurrency;
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
		List<HeldCurrency> heldCurrencies = portfolio.getHeldCurrencies();
		
		// Search through all the held currencies.
		for (HeldCurrency item : heldCurrencies) {
			// Check whether each one contains the desired currency.
			if (item.getCurrency().equals(currency)) {
				// If you find the right one, increase it, then save changes and exit.
				double newBalance = item.getQuantity() + quantity;
				item.setQuantity(newBalance);
				portfolioRepo.save(portfolio);
				return;
			}
		}
		// If you didn't find it, add a new held currency.
		HeldCurrency newItem = new HeldCurrency(portfolio, currency);
		newItem.setQuantity(quantity);
		heldCurrencies.add(newItem);
		portfolioRepo.save(portfolio);
	}

	@Override
	public void decreaseCurrency(Currency currency, Double quantity, int portfolio_id) throws InsufficientFundsException {
		Portfolio portfolio = portfolioRepo.getById(portfolio_id);
		List<HeldCurrency> heldCurrencies = portfolio.getHeldCurrencies();
		
		// Search through all the held currencies.
		for (HeldCurrency item : heldCurrencies) {
			// Check whether each one contains the desired currency.
			if (item.getCurrency().equals(currency)) {
				// If you find the right one, check whether they have enough money.
				double newBalance = item.getQuantity() - quantity;
				if (newBalance < 0) {
					throw new InsufficientFundsException();
				} else {
					// If they have enough, decrease it, then save changes and exit.
					item.setQuantity(newBalance);
					portfolioRepo.save(portfolio);
					return;
				}
			}
		}
		// If you didn't find it, the user doesn't have any money in that currency.
		throw new InsufficientFundsException();
	}

	
}

package com.fdmgroup.Lettuce.Service;

import java.util.List;

import com.fdmgroup.Lettuce.Exceptions.InsufficientFundsException;
import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.Portfolio;

public interface iPortfolio {
	List<Portfolio> getAllPortfolios();
	Portfolio getPortfolioById(int portfolio_id);
	void addPortfolio(Portfolio portfolio);
	void deletePortfolio(Portfolio portfolio);
	void deletePortfolioById(int portfolio_id);
	void deleteAllPortfolios();
	
	void increaseCurrency(Currency currency, Double quantity, int portfolio_id);
	void decreaseCurrency(Currency currency, Double quantity, int portfolio_id) throws InsufficientFundsException;
}

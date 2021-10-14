package com.fdmgroup.Lettuce.Service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.Lettuce.Exceptions.InsufficientFundsException;
import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.HeldCurrency;
import com.fdmgroup.Lettuce.Models.Portfolio;
import com.fdmgroup.Lettuce.Repo.PortfolioRepo;

@Service
public class PortfolioServiceImpl implements iPortfolio {
	public static final Logger dbLogger=LogManager.getLogger("DBLogging");
	
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

	/**
	 * Adds money to a user's portfolio, storing it in the correct HeldCurrency
	 * object. If the user has no money in that currency at all yet, a new
	 * HeldCurrency will be created.
	 * <p>
	 * This method does not verify where the money is coming from. It simply trusts
	 * you to ensure that the money isn't coming from thin air.
	 * 
	 * @param currency     The currency to add to the portfolio.
	 * @param quantity     The amount to add, expressed in that currency.
	 * @param portfolio_id The ID number of the portfolio to add it to.
	 */
	@Override
	public void increaseCurrency(Currency currency, double quantity, int portfolio_id) {
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
				dbLogger.info("Added " + currency + " " + quantity + " to portfolio " + portfolio_id);
				return;
			}
		}
		// If you didn't find it, add a new held currency.
		HeldCurrency newItem = new HeldCurrency(portfolio, currency);
		newItem.setQuantity(quantity);
		heldCurrencies.add(newItem);
		portfolioRepo.save(portfolio);
		dbLogger.info("Added " + currency + " " + quantity + " to portfolio " + portfolio_id);
	}

	/**
	 * Takes money from a user's portfolio, subtracting from the correct
	 * HeldCurrency object.
	 * <p>
	 * This method does not verify where the money is going. It simply trusts you to
	 * ensure the money isn't being thrown into a black hole.
	 * <p>
	 * NOTE: It is recommended that you call this method BEFORE any other methods
	 * that might modify the database, to ensure that the insufficient funds problem
	 * is discovered early and the operation is aborted before any changes are made.
	 * Alternatively, annotate your method with @Transactional(rollbackFor =
	 * {InsufficentFundsException.class}) to ensure it will be aborted safely
	 * regardless of when the problem is discovered.
	 * 
	 * @param currency     The currency to subtract from the portfolio.
	 * @param quantity     The amount to subtract, expressed in that currency.
	 * @param portfolio_id The ID number of the portfolio to add it to.
	 * 
	 * @throws InsufficientFundsException if the user doesn't have enough (or if
	 *                                    they have none at all) of that currency.
	 */
	@Override
	public void decreaseCurrency(Currency currency, double quantity, int portfolio_id)
			throws InsufficientFundsException {
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
					dbLogger.info("Subtracted " + currency + " " + quantity + " from portfolio " + portfolio_id);
					return;
				}
			}
		}
		// If you didn't find it, the user doesn't have any money in that currency.
		throw new InsufficientFundsException();
	}

}

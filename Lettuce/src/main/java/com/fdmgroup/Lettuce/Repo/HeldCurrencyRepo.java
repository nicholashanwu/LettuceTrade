package com.fdmgroup.Lettuce.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.HeldCurrency;
import com.fdmgroup.Lettuce.Models.HeldCurrencyKey;
import com.fdmgroup.Lettuce.Models.Portfolio;

@Repository
public interface HeldCurrencyRepo extends JpaRepository<HeldCurrency, HeldCurrencyKey> {
	
	List<HeldCurrency> findByPortfolio(Portfolio portfolio);
	List<HeldCurrency> findByCurrency(Currency currency);
	HeldCurrency getByPortfolioAndCurrency(Portfolio portfolio, Currency currency);

}

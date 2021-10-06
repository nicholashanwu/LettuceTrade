package com.fdmgroup.Lettuce.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Repo.CurrencyRepo;



public class CurrencyServiceImpl implements ICurrency {

	@Autowired
	CurrencyRepo currencyRepo;

	@Override
	public List<Currency> getAllCurrency() {
		return currencyRepo.findAll();
	}

	@Override
	public Currency getCurrencyById(int id) {
		return currencyRepo.getById(id);
	}

	@Override
	public void addCurrency(Currency currency) {
		currencyRepo.save(currency);
	}

	@Override
	public void updateCurrency(Currency currency, int id) {
		currency.setCurrencyId(id);
		currencyRepo.save(currency);
	}

	@Override
	public void deleteCurrency(int id) {
		currencyRepo.deleteById(id);
	}

}

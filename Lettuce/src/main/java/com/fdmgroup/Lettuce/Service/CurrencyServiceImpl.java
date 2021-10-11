package com.fdmgroup.Lettuce.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Repo.CurrencyRepo;


@Service
public class CurrencyServiceImpl implements iCurrency {

	@Autowired
	CurrencyRepo currencyRepo;

	@Override
	public List<Currency> getAllCurrency() {
		return currencyRepo.findAll();
	}

	@Override
	public Currency getCurrencyById(String id) {
		return currencyRepo.getById(id);
	}

	@Override
	public void addCurrency(Currency currency) {
		currencyRepo.save(currency);
	}

	@Override
	public void updateCurrency(Currency currency, String name) {
		currency.setFullName(name);
		currencyRepo.save(currency);
	}

	@Override
	public void deleteCurrency(String id) {
		currencyRepo.deleteById(id);
	}

}

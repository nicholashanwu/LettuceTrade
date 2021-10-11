package com.fdmgroup.Lettuce.Service;

import java.util.List;

import com.fdmgroup.Lettuce.Models.Currency;

public interface iCurrency {

	List<Currency> getAllCurrency();

	Currency getCurrencyById(String id);

	void addCurrency(Currency currency);

	void updateCurrency(Currency currency, String name);

	void deleteCurrency(String id);

}

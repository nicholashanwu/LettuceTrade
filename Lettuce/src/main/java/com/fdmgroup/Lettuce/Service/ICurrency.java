package com.fdmgroup.Lettuce.Service;

import java.util.List;

import com.fdmgroup.Lettuce.Models.Currency;

public interface ICurrency {

	List<Currency> getAllCurrency();

	Currency getCurrencyById(int id);

	void addCurrency(Currency currency);

	void updateCurrency(Currency currency, int id);

	void deleteCurrency(int id);

}

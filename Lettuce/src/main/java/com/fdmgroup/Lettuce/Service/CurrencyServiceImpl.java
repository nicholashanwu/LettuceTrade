package com.fdmgroup.Lettuce.Service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

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
	public Currency getCurrencyById(String id) throws EntityNotFoundException {
		// Fail fast: check whether the entity exists BEFORE getting it, rather than letting lazy initialisation happen and giving us a surprise exception later. 
		if (currencyRepo.existsById(id)) {
			return currencyRepo.getById(id);
		} else {
			throw new EntityNotFoundException();
		}
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

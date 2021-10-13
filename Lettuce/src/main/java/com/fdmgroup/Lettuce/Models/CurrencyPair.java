package com.fdmgroup.Lettuce.Models;

public class CurrencyPair {
	private Currency firstCurrency;
	private Currency secondCurrency;
	private double rate;
	public CurrencyPair(Currency firstCurrency, Currency secondCurrency, double rate) {
		super();
		this.firstCurrency = firstCurrency;
		this.secondCurrency = secondCurrency;
		this.rate = rate;
	}
	public Currency getFirstCurrency() {
		return firstCurrency;
	}
	public void setFirstCurrency(Currency firstCurrency) {
		this.firstCurrency = firstCurrency;
	}
	public Currency getSecondCurrency() {
		return secondCurrency;
	}
	public void setSecondCurrency(Currency secondCurrency) {
		this.secondCurrency = secondCurrency;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
}

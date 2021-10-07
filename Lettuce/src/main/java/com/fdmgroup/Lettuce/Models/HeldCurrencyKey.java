package com.fdmgroup.Lettuce.Models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class HeldCurrencyKey implements Serializable {
	
	@Column(name = "FK_portfolio")
	private int portfolioId;
	
	@Column(name = "FK_currency")
	private int currencyId;
	
	public HeldCurrencyKey(int portfolioId, int currencyId) {
		super();
		this.portfolioId = portfolioId;
		this.currencyId = currencyId;
	}

	public HeldCurrencyKey() {
		super();
	}

	public int getPortfolioId() {
		return portfolioId;
	}

	public void setPortfolioId(int portfolioId) {
		this.portfolioId = portfolioId;
	}

	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(currencyId, portfolioId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HeldCurrencyKey other = (HeldCurrencyKey) obj;
		return currencyId == other.currencyId && portfolioId == other.portfolioId;
	}

	@Override
	public String toString() {
		return "HeldCurrencyKey [userId=" + portfolioId + ", currencyId=" + currencyId + "]";
	}
	
	

}

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
	private String currencyCode;
	
	public HeldCurrencyKey(int portfolioId, String currencyCode) {
		super();
		this.portfolioId = portfolioId;
		this.currencyCode = currencyCode;
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

	public String getCurrencyId() {
		return currencyCode;
	}

	public void setCurrencyId(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(currencyCode, portfolioId);
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
		return currencyCode.equals(other.currencyCode) && portfolioId == other.portfolioId;
	}

	@Override
	public String toString() {
		return "HeldCurrencyKey [userId=" + portfolioId + ", currencyCode=" + currencyCode + "]";
	}
	
	

}

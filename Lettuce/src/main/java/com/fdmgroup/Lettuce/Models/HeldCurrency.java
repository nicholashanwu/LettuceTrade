package com.fdmgroup.Lettuce.Models;

import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="Lettuce_Held_Currencies")
public class HeldCurrency {
	
	@EmbeddedId
	private HeldCurrencyKey id = new HeldCurrencyKey();
	
	@ManyToOne
	@MapsId("portfolioId")
	@JoinColumn(name = "FK_portfolio")
	private Portfolio portfolio;
	
	@ManyToOne
	@MapsId("currencyId")
	@JoinColumn(name = "FK_currency")
	private Currency currency;
	
	private double quantity;

	public HeldCurrency(Portfolio portfolio, Currency currency) {
		super();
		this.portfolio = portfolio;
		this.currency = currency;
	}

	public HeldCurrency() {
		super();
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "HeldCurrency [id=" + id + ", quantity=" + quantity + "]";
	}

	public boolean equals(HeldCurrency other) {
		return id.equals(other.id);
	}
	
	
	
	
	
	

}

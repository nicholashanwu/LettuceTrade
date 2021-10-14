package com.fdmgroup.Lettuce.Models;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/**
 * Database entity for a single entry in a portfolio. For example, if a user's
 * portfolio contained 200 USD and 350 EUR, then that would be stored as two
 * HeldCurrency objects.
 * <p>
 * Behind the scenes, this entity behaves similarly to a many-to-many database
 * linking table between portfolios and currencies, except that it stores the
 * additional information of HOW MUCH money they hold.
 */
@Entity
@Table(name = "Lettuce_Held_Currency")
public class HeldCurrency {

	@EmbeddedId
	private HeldCurrencyKey id = new HeldCurrencyKey();

	@ManyToOne
	@MapsId("portfolioId")
	@JoinColumn(name = "FK_portfolio")
	private Portfolio portfolio;

	@ManyToOne
	@MapsId("currencyCode")
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

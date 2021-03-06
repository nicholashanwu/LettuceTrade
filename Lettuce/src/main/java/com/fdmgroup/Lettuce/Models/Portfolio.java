package com.fdmgroup.Lettuce.Models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Database entity for a user's portfolio, which stores all of the currencies
 * that they own.
 */
@Entity
@Table(name = "Lettuce_Portfolio")
public class Portfolio {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int portfolioId;

	@OneToOne
	@JoinColumn(name = "FK_user")
	private User user;

	@OneToMany(mappedBy = "portfolio", fetch = FetchType.EAGER)
	private List<HeldCurrency> heldCurrencies = new ArrayList<>();

	public Portfolio() {
		super();
	}

	public Portfolio(User user) {
		super();
		this.user = user;
	}

	public int getPortfolioId() {
		return portfolioId;
	}

	public void setPortfolioId(int portfolioId) {
		this.portfolioId = portfolioId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<HeldCurrency> getHeldCurrencies() {
		return heldCurrencies;
	}

	public void setHeldCurrencies(List<HeldCurrency> heldCurrencies) {
		this.heldCurrencies = heldCurrencies;
	}

	@Override
	public String toString() {
		return "Portfolio [portfolioId=" + portfolioId +
//				", heldCurrencies=" + heldCurrencies + 
				"]";
	}

}

package com.fdmgroup.Lettuce.Models;

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
	@MapsId("userId")
	@JoinColumn(name = "FK_user")
	private User user;
	
	@ManyToOne
	@MapsId("currencyId")
	@JoinColumn(name = "FK_currency")
	private Currency currency;
	
	private double quantity;

	public HeldCurrency(User user, Currency currency) {
		super();
		this.user = user;
		this.currency = currency;
	}

	public HeldCurrency() {
		super();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
	
	
	
	

}

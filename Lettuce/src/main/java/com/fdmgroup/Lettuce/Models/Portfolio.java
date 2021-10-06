package com.fdmgroup.Lettuce.Models;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.ToString;

@ToString
@Entity
@Table(name="Lettuce_Portfolio")
public class Portfolio {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int portfolio_id;
	
//	@OneToOne(mappedBy="portfolio")
//	private User user;
//	@ManyToMany
//	private  Map<Currency, Double> currencies_held;
	
	public Portfolio() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Portfolio(User user, Map<Currency, Double> currencies_held) {
		super();
//		this.user = user;
//		this.currencies_held = currencies_held;
	}
	public int getPortfolio_id() {
		return portfolio_id;
	}
//	public User getUser() {
//		return user;
//	}
//	public void setUser(User user) {
//		this.user = user;
//	}
//	public Map<Currency, Double> getCurrencies_held() {
//		return currencies_held;
//	}
//	public void setCurrencies_held(Map<Currency, Double> currencies_held) {
//		this.currencies_held = currencies_held;
//	}
	
	
}

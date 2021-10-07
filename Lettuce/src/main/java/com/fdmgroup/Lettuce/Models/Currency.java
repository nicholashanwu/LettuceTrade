package com.fdmgroup.Lettuce.Models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



@Entity
@Table(name = "Currency")
public class Currency {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int currencyId;
	private String currencyName;

	/**
	 * @param currencyName
	 */
	public Currency(String currencyName) {
		super();
		this.currencyName = currencyName;
	}

	public Currency() {
		super();
	}

	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	@Override
	public String toString() {
		return "Currency [currencyId=" + currencyId + ", currencyName=" + currencyName + "]";
	}

}

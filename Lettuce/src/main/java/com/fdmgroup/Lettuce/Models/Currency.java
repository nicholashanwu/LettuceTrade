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
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currencySeq")
	@SequenceGenerator(name = "currencySeq", sequenceName = "LettureSeq1", initialValue = 0, allocationSize = 1)
	private int currency_id;
	private String currency_name;

	/**
	 * @param currency_name
	 */
	public Currency(String currency_name) {
		super();
		this.currency_name = currency_name;
	}

	public int getCurrency_id() {
		return currency_id;
	}

	public void setCurrency_id(int currency_id) {
		this.currency_id = currency_id;
	}

	public String getCurrency_name() {
		return currency_name;
	}

	public void setCurrency_name(String currency_name) {
		this.currency_name = currency_name;
	}

	@Override
	public String toString() {
		return "Currency [currency_id=" + currency_id + ", currency_name=" + currency_name + "]";
	}

}

package com.fdmgroup.Lettuce.Models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Lettuce_Currency")
public class Currency {
	
	@Id
	private String currencyCode;
	private String fullName;
	private String imageLocation;

	public Currency() {
		super();
	}

	public Currency(String currencyCode, String fullName) {
		super();
		this.currencyCode = currencyCode;
		this.fullName = fullName;
	}

	public Currency(String currencyCode) {
		super();
		this.currencyCode = currencyCode;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getImageLocation() {
		return imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}

	@Override
	public String toString() {
		return currencyCode;
	}
	
	
	
}



//@Entity
//@Table(name = "Lettuce_Currency")
//public class Currency {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private int currencyId;
//	private String currencyName;
//
//	/**
//	 * @param currencyName
//	 */
//	public Currency(String currencyName) {
//		super();
//		this.currencyName = currencyName;
//	}
//
//	public Currency() {
//		super();
//	}
//
//	public int getCurrencyId() {
//		return currencyId;
//	}
//
//	public void setCurrencyId(int currencyId) {
//		this.currencyId = currencyId;
//	}
//
//	public String getCurrencyName() {
//		return currencyName;
//	}
//
//	public void setCurrencyName(String currencyName) {
//		this.currencyName = currencyName;
//	}
//
//	@Override
//	public String toString() {
//		return currencyName;
//	}
//
//}

package com.fdmgroup.Lettuce.Models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class HeldCurrencyKey implements Serializable {
	
	@Column(name = "FK_user")
	private int userId;
	
	@Column(name = "FK_currency")
	private int currencyId;
	
	public HeldCurrencyKey(int userId, int currencyId) {
		super();
		this.userId = userId;
		this.currencyId = currencyId;
	}

	public HeldCurrencyKey() {
		super();
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(currencyId, userId);
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
		return currencyId == other.currencyId && userId == other.userId;
	}

	@Override
	public String toString() {
		return "HeldCurrencyKey [userId=" + userId + ", currencyId=" + currencyId + "]";
	}
	
	

}

package com.fdmgroup.Lettuce.Models;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Lettuce_Forward_Orders")
public class ForwardOrder extends Order {
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate scheduledDate;
	private double agreedPrice;
	
	public LocalDate getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(LocalDate scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public double getAgreedPrice() {
		return agreedPrice;
	}

	public void setAgreedPrice(double agreedPrice) {
		this.agreedPrice = agreedPrice;
	}

	@Override
	public boolean matches(Order otherOrder) {
		if (otherOrder instanceof ForwardOrder) {
			ForwardOrder other = (ForwardOrder) otherOrder; // Cast it so that we can access the date.
			boolean dateMatches = this.scheduledDate.equals(other.scheduledDate);
			boolean firstCurrencyMatches = this.getBaseCurrency().equals(other.getTargetCurrency());
			boolean secondCurrencyMatches = this.getTargetCurrency().equals(other.getBaseCurrency());
			return (dateMatches && firstCurrencyMatches && secondCurrencyMatches);
		} else {
			return false;
		}
	}

}

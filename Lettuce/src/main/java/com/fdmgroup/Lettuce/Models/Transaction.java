package com.fdmgroup.Lettuce.Models;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

/**
 * Records a foreign exchange transaction between two Orders, including the
 * exact time when the transaction took place and how much of each currency was
 * exchanged. Sequence is not guaranteed: the end user may be either order1 or
 * order2, because two different end users can view the same Transaction object.
 * However, sequence is internally consistent: order1 sold quantity1 of
 * currency1.
 * 
 * @author Simon Whelan
 */
@Entity
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int transactionId;
	private LocalDateTime time;
	@ManyToOne
	@JoinTable(name = "FK_order_1")
	private Order order1;
	@ManyToOne
	@JoinTable(name = "FK_order_2")
	private Order order2;
	@ManyToOne
	@JoinTable(name = "FK_currency_1")
	private Currency currency1;
	@ManyToOne
	@JoinTable(name = "FK_currency_2")
	private Currency currency2;
	private double quantity1;
	private double quantity2;

	public Transaction() {
		super();
	}

	public Transaction(Order order1, Order order2, Currency currency1, Currency currency2, double quantity1,
			double quantity2) {
		super();
		this.time = LocalDateTime.now();
		this.order1 = order1;
		this.order2 = order2;
		this.currency1 = currency1;
		this.currency2 = currency2;
		this.quantity1 = quantity1;
		this.quantity2 = quantity2;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public Order getOrder1() {
		return order1;
	}

	public void setOrder1(Order order1) {
		this.order1 = order1;
	}

	public Order getOrder2() {
		return order2;
	}

	public void setOrder2(Order order2) {
		this.order2 = order2;
	}

	public Currency getCurrency1() {
		return currency1;
	}

	public void setCurrency1(Currency currency1) {
		this.currency1 = currency1;
	}

	public Currency getCurrency2() {
		return currency2;
	}

	public void setCurrency2(Currency currency2) {
		this.currency2 = currency2;
	}

	public double getQuantity1() {
		return quantity1;
	}

	public void setQuantity1(double quantity1) {
		this.quantity1 = quantity1;
	}

	public double getQuantity2() {
		return quantity2;
	}

	public void setQuantity2(double quantity2) {
		this.quantity2 = quantity2;
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", time=" + time + ", orderID " + order1.getOrderId()
				+ " " + currency1 + quantity1 + " traded for orderID " + order2.getOrderId() + " " + currency2
				+ quantity2 + "]";
	}

}

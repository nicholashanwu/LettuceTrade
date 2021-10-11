package com.fdmgroup.Lettuce.Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Lettuce_Order")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int orderId;
	@ManyToOne
	@JoinColumn(name = "FK_userId")
	private User user;
	@ManyToOne
	@JoinColumn(name = "FK_baseCurrency")
	private Currency baseCurrency;
	@ManyToOne
	@JoinColumn(name = "FK_targetCurrency")
	private Currency targetCurrency;
	private OrderType orderType;
	private OrderStatus orderStatus;
	private double quantity;
	private double initialQuantity;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate expiryDate;
	private LocalDate scheduledDate;
	@OneToMany(mappedBy = "order1")
	private List<Transaction> transactionsAsParty1 = new ArrayList<>();
	@OneToMany(mappedBy = "order2")
	private List<Transaction> transactionsAsParty2 = new ArrayList<>();

	public Order() {
		super();
	}

	public Order(User user, Currency baseCurrency, Currency targetCurrency, OrderType orderType, double initialQuantity,
			LocalDate expiryDate) {
		super();
		this.user = user;
		this.baseCurrency = baseCurrency;
		this.targetCurrency = targetCurrency;
		this.orderType = orderType;
		this.orderStatus = OrderStatus.PENDING;
		this.initialQuantity = initialQuantity;
		this.quantity = initialQuantity;
		this.expiryDate = expiryDate;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Currency getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(Currency baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public Currency getTargetCurrency() {
		return targetCurrency;
	}

	public void setTargetCurrency(Currency targetCurrency) {
		this.targetCurrency = targetCurrency;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getInitialQuantity() {
		return initialQuantity;
	}

	public void setInitialQuantity(double initialQuantity) {
		this.initialQuantity = initialQuantity;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public LocalDate getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(LocalDate scheduledDate) {
		this.scheduledDate = scheduledDate;
	}
	
	
	public List<Transaction> getTransactionsAsParty1() {
		return transactionsAsParty1;
	}

	public void setTransactionsAsParty1(List<Transaction> transactionsAsParty1) {
		this.transactionsAsParty1 = transactionsAsParty1;
	}

	public List<Transaction> getTransactionsAsParty2() {
		return transactionsAsParty2;
	}

	public void setTransactionsAsParty2(List<Transaction> transactionsAsParty2) {
		this.transactionsAsParty2 = transactionsAsParty2;
	}
	
	public List<Transaction> getTransactions() {
		List<Transaction> list = new ArrayList<>();
		list.addAll(transactionsAsParty1);
		list.addAll(transactionsAsParty2);
		return list;
	}

	public boolean isOppositeCurrency(Order otherOrder) {
		return (this.baseCurrency.equals(otherOrder.targetCurrency) && this.targetCurrency.equals(otherOrder.baseCurrency));
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", baseCurrency=" + baseCurrency + ", targetCurrency=" + targetCurrency
				+ ", quantity=" + quantity + "]";
	}
	
	
}

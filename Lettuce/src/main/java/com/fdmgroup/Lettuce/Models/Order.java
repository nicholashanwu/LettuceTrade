package com.fdmgroup.Lettuce.Models;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

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
	private LocalDate expiryDate;
	private LocalDate scheduledDate;

	public Order() {
		super();
	}

	public Order(User user, Currency baseCurrency, Currency targetCurrency, OrderType orderType, double quantity,
			LocalDate expiryDate) {
		super();
		this.user = user;
		this.baseCurrency = baseCurrency;
		this.targetCurrency = targetCurrency;
		this.orderType = orderType;
		this.quantity = quantity;
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
	
	
}

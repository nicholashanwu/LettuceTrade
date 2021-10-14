package com.fdmgroup.Lettuce.Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Database entity for when a user places a foreign exchange order. When an
 * order is resolved, the results will be recorded in a Transaction object.
 */
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
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate scheduledDate; // Intended for forward orders. Leave it null for spot orders.
	@OneToMany(mappedBy = "order1")
	private List<Transaction> transactionsAsParty1 = new ArrayList<>();
	@OneToMany(mappedBy = "order2")
	private List<Transaction> transactionsAsParty2 = new ArrayList<>();
	private String details;

	public Order() {
		super();
	}

	public Order(User user, Currency baseCurrency, Currency targetCurrency, OrderType orderType, double initialQuantity,
			LocalDate expiryDate, String details) {
		super();
		this.user = user;
		this.baseCurrency = baseCurrency;
		this.targetCurrency = targetCurrency;
		this.orderType = orderType;
		this.orderStatus = OrderStatus.ACTIVE;
		this.initialQuantity = initialQuantity;
		this.quantity = initialQuantity;
		this.expiryDate = expiryDate;
		this.details = details;
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

	public LocalDate getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(LocalDate scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * Determines whether two orders 'match', based on the following criteria:
	 * <ul>
	 * <li>Each order must have the same currencies, but reversed. For example, a
	 * USD->EUR order matches with an EUR->USD order.</li>
	 * <li>Both orders must be the same OrderType.</li>
	 * <li>If they're OrderType.FORWARD then they must be scheduled for the same
	 * date.</li>
	 * </ul>
	 * 
	 * @param otherOrder The order to compare this one against.
	 * @return whether the two orders match.
	 */
	public boolean matches(Order otherOrder) {
		if (this.orderType != otherOrder.orderType) {
			return false;
		}
		boolean firstCurrencyMatches = this.baseCurrency.equals(otherOrder.targetCurrency);
		boolean secondCurrencyMatches = this.targetCurrency.equals(otherOrder.baseCurrency);
		switch (orderType) {
		case SPOT:
			return (firstCurrencyMatches && secondCurrencyMatches);
		case FORWARD:
			boolean dateMatches = this.scheduledDate.equals(otherOrder.scheduledDate);
			return (dateMatches && firstCurrencyMatches && secondCurrencyMatches);
		default:
			// Should be unreachable.
			return false;
		}
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", baseCurrency=" + baseCurrency + ", targetCurrency=" + targetCurrency
				+ ", quantity=" + quantity + "]";
	}

}

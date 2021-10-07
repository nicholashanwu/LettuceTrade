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
	private OrderType orderType;
	private double ppu;
	private double quantity; // May need to change, ASK CALEB
	private LocalDate expiryDate;
	private LocalDate scheduledDate;
	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "FK_userId")
	private User user;

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", orderType=" + orderType + ", ppu=" + ppu + ", quantity=" + quantity
				+ ", expiryDate=" + expiryDate + ", scheduledDate=" + scheduledDate + ", user=" + user + "]";
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public double getPpu() {
		return ppu;
	}

	public void setPpu(double ppu) {
		this.ppu = ppu;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getOrderId() {
		return orderId;
	}

	public Order(OrderType orderType, double ppu, double quantity, LocalDate expiryDate, LocalDate scheduledDate, User user) {
		super();
		this.orderType = orderType;
		this.ppu = ppu;
		this.quantity = quantity;
		this.expiryDate = expiryDate;
		this.scheduledDate = scheduledDate;
		this.user = user;
	}

	public Order() {
		super();
	}
}

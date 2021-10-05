package com.fdmgroup.Lettuce.Models;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@Entity
@Table(name = "Lettuce_Order")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(value = AccessLevel.NONE)
	private int orderId;
	
	private OrderType orderType;
	private double ppu;
	private double quantity; //May need to change, ASK CALEB
	private LocalDate expiryDate;
	private LocalDate scheduledDate;
	
	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "FK_userId") private User user;
	 */
	
	
	public Order( OrderType orderType, double ppu, double quantity, LocalDate expiryDate,
			LocalDate scheduledDate) {
		super();
		this.orderType = orderType;
		this.ppu = ppu;
		this.quantity = quantity;
		this.expiryDate = expiryDate;
		this.scheduledDate = scheduledDate;
	}

	public Order() {
		super();
	}
}

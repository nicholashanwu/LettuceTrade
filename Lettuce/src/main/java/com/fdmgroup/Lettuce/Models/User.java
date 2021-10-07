package com.fdmgroup.Lettuce.Models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int userId;
	
	@Column(unique=true)
	private String userName;
	private String name;
	
	private String password;
	private double bankAccountBalance;
	private boolean Admin;
//TODO
//	@OneToOne(mappedBy="user")
//	private Portfolio portfolio;
//	
//	@OneToMany(mappedBy = "user") 
//	private List<Order> orders = new ArrayList<>();
	
	public User() {
		super();
	}
	
	public User(String userName, String name, String password, double bankAccountBalance, boolean admin) {
		super();
		this.userName = userName;
		this.name = name;
		this.password = password;
		this.bankAccountBalance = bankAccountBalance;
		Admin = admin;
	}

	public User(String userName, String password, boolean isAdmin) {
		super();
		this.userName = userName;
		this.password = password;
		this.Admin = isAdmin;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public double getBankAccountBalance() {
		return bankAccountBalance;
	}
	public void setBankAccountBalance(double bankAccountBalance) {
		this.bankAccountBalance = bankAccountBalance;
	}
	public boolean isAdmin() {
		return Admin;
	}
	public void setAdmin(boolean isAdmin) {
		this.Admin = isAdmin;
	}
	//TODO
//	public Portfolio getPortfolio() {
//		return portfolio;
//	}
//	public void setPortfolio(Portfolio portfolio) {
//		this.portfolio = portfolio;
//	}
//	public List<Order> getOrders() {
//		return orders;
//	}
//	public void setOrders(List<Order> orders) {
//		this.orders = orders;
//	}
	@Override
	public String toString() {
		return "User [userName=" + userName + ", bankAccountBalance=" + bankAccountBalance + ", isAdmin=" + Admin
				+ "]";
	}
	
	
}

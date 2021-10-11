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
import javax.persistence.Table;

@Entity
@Table(name = "Lettuce_User")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int userId;

	@Column(unique = true)
	private String email;
	private String firstName;
	private String lastName;
	private String password;
	private String admin;
    @Column(name = "verification_code", updatable = false)
    private String verificationCode;
   
    private String enabled;

	@OneToOne(mappedBy = "user")
	private Portfolio portfolio;

	@OneToMany(mappedBy = "user")
	private List<Order> orders = new ArrayList<>();

	public User() {
		super();
	}

	

	public User( String email, String firstName, String lastName, String password, String admin) {
		super();

		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.admin = admin;
		//this.portfolio = portfolio;
		//this.orders = orders;
	}



	public User(int userId, String email, String firstName, String lastName, String password, String admin,
			String verificationCode, String enabled) {
		super();
		this.userId = userId;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.admin = admin;
		this.verificationCode = verificationCode;
		this.enabled = enabled;
	}



	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String isAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public String getVerificationCode() {
		return verificationCode;
	}



	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}



	public String getEnabled() {
		return enabled;
	}



	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}



	@Override
	public String toString() {
		return "User [email=" + email + 
				", firstName=" + firstName + 
				", lastName=" + lastName + 
				", admin=" + admin +
				", portfolio=" + portfolio +
			"]";
	}

}

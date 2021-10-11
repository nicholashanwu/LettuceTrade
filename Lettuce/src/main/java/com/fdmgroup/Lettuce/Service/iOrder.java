package com.fdmgroup.Lettuce.Service;

import java.util.List;

import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.User;

public interface iOrder {
	public List<Order> getAllOrders();
	public List<Order> getAllOrdersForUser(User user);
	public List<Order> getOutstandingOrders();
	public List<Order> getOutstandingOrdersForUser(User user);
	public List<Order> getClosedOrders();
	public List<Order> getClosedOrdersForUser(User user);
	public Order getOrderById(int id);
	
	public void addOrder(Order order);
	public void updateOrder(Order order);
	public void deleteOrder(Order order);
	public void deleteOrderById(int id);
	
	public void tryToMatch(Order order);
}

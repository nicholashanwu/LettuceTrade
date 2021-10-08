package com.fdmgroup.Lettuce.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.OrderStatus;
import com.fdmgroup.Lettuce.Models.User;
import com.fdmgroup.Lettuce.Repo.OrderRepo;
import com.fdmgroup.Lettuce.rates.ExchangeRate;

public class OrderServiceImpl implements iOrder {
	@Autowired
	OrderRepo repo;

	@Override
	public List<Order> getAllOrders() {
		return repo.findAll();
	}

	@Override
	public List<Order> getAllOrdersForUser(User user) {
		return repo.orderByUser(user);
	}

	@Override
	public List<Order> getOutstandingOrders() {
		List<Order> pending = repo.orderByStatus(OrderStatus.PENDING);
		List<Order> partial = repo.orderByStatus(OrderStatus.PARTIALLY_COMPLETE);
		pending.addAll(partial);
		return pending;
	}

	@Override
	public Order getOrderById(int id) {
		return repo.getById(id);
	}

	@Override
	public void addOrder(Order order) {
		repo.save(order);
	}

	@Override
	public void updateOrder(Order order) {
		repo.save(order);
	}

	@Override
	public void deleteOrder(Order order) {
		repo.delete(order);
	}

	@Override
	public void deleteOrderById(int id) {
		repo.deleteById(id);
	}

	@Override
	public void tryToMatch(Order order) {
		List<Order> orders = getOutstandingOrders();
		for (Order orderFound : orders) {
			if (order.isOppositeCurrency(orderFound)) {
				// We've found a match!
				// Determine how much currency to exchange.
				try {
					double exchangeRate = ExchangeRate.getRateForPair(order.getBaseCurrency().toString(),
							order.getTargetCurrency().toString());
					double availableToSell = order.getQuantity();
					double wantToBuy = order.getQuantity() * exchangeRate;
					double availableToBuy = orderFound.getQuantity();
					double wantToSell = orderFound.getQuantity() / exchangeRate;
					if (wantToBuy < availableToBuy) {
						// Consume the entire order, and consume part of the orderFound.
						order.setQuantity(0);
						order.setOrderStatus(OrderStatus.COMPLETE);
						orderFound.setQuantity(availableToBuy - wantToBuy);
						orderFound.setOrderStatus(OrderStatus.PARTIALLY_COMPLETE);
					} else if (wantToBuy > availableToBuy) {
						// Consume the entire orderFound, and consume part of the order.
						order.setQuantity(availableToSell - wantToSell);
						order.setOrderStatus(OrderStatus.PARTIALLY_COMPLETE);
						orderFound.setQuantity(0);
						orderFound.setOrderStatus(OrderStatus.COMPLETE);
					} else if (wantToBuy == availableToBuy) {
						// Consume both.
						order.setQuantity(0);
						order.setOrderStatus(OrderStatus.COMPLETE);
						orderFound.setQuantity(0);
						orderFound.setOrderStatus(OrderStatus.COMPLETE);
					}
					// Save changes.
					repo.save(order);
					repo.save(orderFound);
					// TODO transactions
					// TODO logging
				} catch (IOException e) {
					// Couldn't contact API. Do nothing.
					// TODO logging
				}
			}
		}
		// No match found. Do nothing.
		// TODO logging
	}

}

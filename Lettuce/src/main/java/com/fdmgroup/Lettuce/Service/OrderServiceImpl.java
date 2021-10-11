package com.fdmgroup.Lettuce.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.OrderStatus;
import com.fdmgroup.Lettuce.Models.Transaction;
import com.fdmgroup.Lettuce.Models.User;
import com.fdmgroup.Lettuce.Repo.OrderRepo;
import com.fdmgroup.Lettuce.Repo.TransactionRepo;
import com.fdmgroup.Lettuce.rates.ExchangeRate;

@Service
public class OrderServiceImpl implements iOrder {
	@Autowired
	OrderRepo orderRepo;

	@Autowired
	TransactionRepo transRepo;

	@Override
	public List<Order> getAllOrders() {
		return orderRepo.findAll();
	}

	@Override
	public List<Order> getAllOrdersForUser(User user) {
		return orderRepo.orderByUser(user);
	}

	@Override
	public List<Order> getOutstandingOrders() {
		List<Order> pending = orderRepo.orderByStatus(OrderStatus.PENDING);
		List<Order> partial = orderRepo.orderByStatus(OrderStatus.PARTIALLY_COMPLETE);
		pending.addAll(partial);
		return pending;
	}

	@Override
	public Order getOrderById(int id) {
		return orderRepo.getById(id);
	}

	@Override
	public void addOrder(Order order) {
		orderRepo.save(order);
	}

	@Override
	public void updateOrder(Order order) {
		orderRepo.save(order);
	}

	@Override
	public void deleteOrder(Order order) {
		orderRepo.delete(order);
	}

	@Override
	public void deleteOrderById(int id) {
		orderRepo.deleteById(id);
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

					double firstUserHas = order.getQuantity(); // Expressed in first user's base currency.
					double firstUserWants = order.getQuantity() * exchangeRate; // Expressed in first user's target
																				// currency.

					double secondUserHas = orderFound.getQuantity(); // Expressed in second user's base currency.
					double secondUserWants = orderFound.getQuantity() / exchangeRate; // Expressed in second user's
																						// target currency.

					if (firstUserWants < secondUserHas) {
						// Consume the entire order, and consume part of the orderFound.
						order.setQuantity(0);
						order.setOrderStatus(OrderStatus.COMPLETE);
						orderFound.setQuantity(secondUserHas - firstUserWants);
						orderFound.setOrderStatus(OrderStatus.PARTIALLY_COMPLETE);
						transRepo.save(new Transaction(order, orderFound, order.getBaseCurrency(),
								orderFound.getBaseCurrency(), firstUserHas, firstUserWants));

					} else if (firstUserWants > secondUserHas) {
						// Consume the entire orderFound, and consume part of the order.
						order.setQuantity(firstUserHas - secondUserWants);
						order.setOrderStatus(OrderStatus.PARTIALLY_COMPLETE);
						orderFound.setQuantity(0);
						orderFound.setOrderStatus(OrderStatus.COMPLETE);
						transRepo.save(new Transaction(order, orderFound, order.getBaseCurrency(),
								orderFound.getBaseCurrency(), secondUserWants, secondUserHas));

					} else if (firstUserWants == secondUserHas) {
						// Consume both.
						order.setQuantity(0);
						order.setOrderStatus(OrderStatus.COMPLETE);
						orderFound.setQuantity(0);
						orderFound.setOrderStatus(OrderStatus.COMPLETE);
						transRepo.save(new Transaction(order, orderFound, order.getBaseCurrency(),
								orderFound.getBaseCurrency(), firstUserHas, secondUserHas));
					}
					// Save changes.
					orderRepo.save(order);
					orderRepo.save(orderFound);
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

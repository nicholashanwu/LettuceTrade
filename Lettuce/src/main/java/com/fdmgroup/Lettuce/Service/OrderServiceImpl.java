package com.fdmgroup.Lettuce.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.Lettuce.Exceptions.InsufficientFundsException;
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
	
	@Autowired
	PortfolioServiceImpl psi;

	@Override
	public List<Order> getAllOrders() {
		return orderRepo.findAll();
	}

	@Override
	public List<Order> getAllOrdersForUser(User user) {
		return orderRepo.getByUser(user);
	}

	@Override
	public List<Order> getOutstandingOrders() {
		List<Order> pending = orderRepo.getByStatus(OrderStatus.PENDING);
		List<Order> partial = orderRepo.getByStatus(OrderStatus.PARTIALLY_COMPLETE);
		pending.addAll(partial);
		return pending;
	}

	@Override
	public List<Order> getOutstandingOrdersForUser(User user) {
		List<Order> pending = orderRepo.getByUserAndStatus(user, OrderStatus.PENDING);
		List<Order> partial = orderRepo.getByUserAndStatus(user, OrderStatus.PARTIALLY_COMPLETE);
		pending.addAll(partial);
		return pending;
	}

	@Override
	public List<Order> getClosedOrders() {
		List<Order> closed = orderRepo.getByStatus(OrderStatus.COMPLETE);
		List<Order> cancelled = orderRepo.getByStatus(OrderStatus.CANCELLED);
		List<Order> expired = orderRepo.getByStatus(OrderStatus.EXPIRED);
		closed.addAll(cancelled);
		closed.addAll(expired);
		return closed;
	}

	@Override
	public List<Order> getClosedOrdersForUser(User user) {
		List<Order> closed = orderRepo.getByUserAndStatus(user, OrderStatus.COMPLETE);
		List<Order> cancelled = orderRepo.getByUserAndStatus(user, OrderStatus.CANCELLED);
		List<Order> expired = orderRepo.getByUserAndStatus(user, OrderStatus.EXPIRED);
		closed.addAll(cancelled);
		closed.addAll(expired);
		return closed;
	}

	@Override
	public Order getOrderById(int id) {
		return orderRepo.getById(id);
	}

	@Override
	public void addOrder(Order order) throws InsufficientFundsException {
		psi.decreaseCurrency(order.getBaseCurrency(), order.getQuantity(), order.getUser().getPortfolio().getPortfolioId());
		orderRepo.save(order);
		tryToMatch(order);
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
			if (order.matches(orderFound)) {
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
						// First user gets everything they want. Second user gets everything the first user has.
						// Consume the entire order, and consume part of the orderFound.
						order.setQuantity(0);
						order.setOrderStatus(OrderStatus.COMPLETE);
						orderFound.setQuantity(secondUserHas - firstUserWants);
						orderFound.setOrderStatus(OrderStatus.PARTIALLY_COMPLETE);
						// Record the changes in a transaction object.
						transRepo.save(new Transaction(order, orderFound, order.getBaseCurrency(),
								orderFound.getBaseCurrency(), firstUserHas, firstUserWants));
						// Give each user the currency they earned.
						psi.increaseCurrency(order.getTargetCurrency(), firstUserWants, order.getUser().getPortfolio().getPortfolioId());
						psi.increaseCurrency(orderFound.getTargetCurrency(), firstUserHas, orderFound.getUser().getPortfolio().getPortfolioId());

					} else if (firstUserWants > secondUserHas) {
						// Second user gets everything they want. First user gets everything the second user has.
						// Consume the entire orderFound, and consume part of the order.
						order.setQuantity(firstUserHas - secondUserWants);
						order.setOrderStatus(OrderStatus.PARTIALLY_COMPLETE);
						orderFound.setQuantity(0);
						orderFound.setOrderStatus(OrderStatus.COMPLETE);
						// Record the changes in a transaction object.
						transRepo.save(new Transaction(order, orderFound, order.getBaseCurrency(),
								orderFound.getBaseCurrency(), secondUserWants, secondUserHas));
						// Give each user the currency they earned.
						psi.increaseCurrency(order.getTargetCurrency(), secondUserHas, order.getUser().getPortfolio().getPortfolioId());
						psi.increaseCurrency(orderFound.getTargetCurrency(), secondUserWants, orderFound.getUser().getPortfolio().getPortfolioId());

					} else if (firstUserWants == secondUserHas) {
						// Both users get everything they want.
						// Consume both.
						order.setQuantity(0);
						order.setOrderStatus(OrderStatus.COMPLETE);
						orderFound.setQuantity(0);
						orderFound.setOrderStatus(OrderStatus.COMPLETE);
						// Record the changes in a transaction object.
						transRepo.save(new Transaction(order, orderFound, order.getBaseCurrency(),
								orderFound.getBaseCurrency(), firstUserHas, secondUserHas));
						// Give each user the currency they earned.
						psi.increaseCurrency(order.getTargetCurrency(), firstUserWants, order.getUser().getPortfolio().getPortfolioId());
						psi.increaseCurrency(orderFound.getTargetCurrency(), secondUserWants, orderFound.getUser().getPortfolio().getPortfolioId());
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

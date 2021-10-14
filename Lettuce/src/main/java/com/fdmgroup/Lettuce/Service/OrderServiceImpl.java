package com.fdmgroup.Lettuce.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.Lettuce.Exceptions.InsufficientFundsException;
import com.fdmgroup.Lettuce.Exceptions.InvalidDateException;
import com.fdmgroup.Lettuce.Exceptions.RecursiveTradeException;
import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.OrderStatus;
import com.fdmgroup.Lettuce.Models.OrderType;
import com.fdmgroup.Lettuce.Models.Transaction;
import com.fdmgroup.Lettuce.Models.TransactionStatus;
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
		List<Order> pending = orderRepo.getByStatus(OrderStatus.ACTIVE);
		List<Order> partial = orderRepo.getByStatus(OrderStatus.PARTIALLY_COMPLETE);
		pending.addAll(partial);
		return pending;
	}

	@Override
	public List<Order> getOutstandingOrdersForUser(User user) {
		List<Order> pending = orderRepo.getByUserAndStatus(user, OrderStatus.ACTIVE);
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
	public List<Order> getAllOrdersNotUser(User user, OrderStatus pend, OrderStatus part) {
		List<Order> outstanding = orderRepo.getAllOrdersNotUser(user, pend, part);// , ot, os);
		return outstanding;
	}

	@Override
	public Order getOrderById(int id) {
		return orderRepo.getById(id);
	}

	/**
	 * Adds a new order to the database, and handles all necessary processing -
	 * checks whether it's a valid order, subtracts money from the user's portfolio,
	 * and matches the order up with any opposite orders.
	 * 
	 * @param order The new order to save.
	 * @throws InsufficientFundsException If the user's portfolio doesn't have
	 *                                    enough money in the base currency.
	 * @throws RecursiveTradeException    If the base currency and target currency
	 *                                    are the same.
	 * @throws InvalidDateException       If it's a forward order with a scheduled
	 *                                    date before its expiry date. An order
	 *                                    should not remain on the market,
	 *                                    un-expired, if its scheduled date has
	 *                                    already passed.
	 */
	@Override
	public void addOrder(Order order) throws InsufficientFundsException, RecursiveTradeException, InvalidDateException {
		// Make sure there aren't any expired orders lingering in the system.
		expireAll();
		// Throw exception if they're trying to trade a currency for itself
		if (order.getBaseCurrency().equals(order.getTargetCurrency())) {
			throw new RecursiveTradeException();
		}
		// Throw exception if the market posting is scheduled to stick around even after
		// the scheduled trade date
		if (order.getOrderType() == OrderType.FORWARD && order.getExpiryDate().isAfter(order.getScheduledDate())) {
			throw new InvalidDateException();
		}
		// Throw exception if there's not enough money available
		psi.decreaseCurrency(order.getBaseCurrency(), order.getQuantity(),
				order.getUser().getPortfolio().getPortfolioId());
		orderRepo.save(order);
		tryToMatch(order);
	}

	@Override
	public void updateOrder(Order order) {
		orderRepo.save(order);
	}

	/**
	 * @deprecated Orders should not be deleted from the database. Use cancelOrder() or expireOrder() instead.
	 */
	@Override
	public void deleteOrder(Order order) {
		orderRepo.delete(order);
	}

	/**
	 * @deprecated Orders should not be deleted from the database. Use cancelOrder() or expireOrder() instead.
	 */
	@Override
	public void deleteOrderById(int id) {
		orderRepo.deleteById(id);
	}

	/**
	 * Sets a single order's OrderStatus to CANCELLED, and refunds money to the
	 * user's portfolio. Use this method when a user manually aborts an order.
	 * 
	 * @param order The order to cancel.
	 */
	@Override
	public void cancelOrder(Order order) {
		psi.increaseCurrency(order.getBaseCurrency(), order.getQuantity(),
				order.getUser().getPortfolio().getPortfolioId());
		order.setOrderStatus(OrderStatus.CANCELLED);
		orderRepo.save(order);
	}

	/**
	 * Sets a single order's OrderStatus to EXPIRED, and refunds money to the user's
	 * portfolio. Use this method when an order is aborted by the system due to
	 * passing its expiry date.
	 * 
	 * @param order The order to expire.
	 */
	@Override
	public void expireOrder(Order order) {
		psi.increaseCurrency(order.getBaseCurrency(), order.getQuantity(),
				order.getUser().getPortfolio().getPortfolioId());
		order.setOrderStatus(OrderStatus.EXPIRED);
		orderRepo.save(order);
	}

	/**
	 * Attempts to match up an order with any opposite orders that are currently
	 * outstanding. For example, if passed a USD->EUR order, this method attempts to
	 * find an available EUR->USD order to trade with.
	 * <p>
	 * Once found, this method calculates the amount to exchange at current market
	 * rates and records the result in a new Transaction object. For SPOT orders, it
	 * will also move funds into each user's portfolio immediately. To move the
	 * funds in a FORWARD order, refer to the Transaction Service.
	 * <p>
	 * If the first match is insufficient to complete order, it will continue to
	 * iterate through the rest of the list searching for more matches.
	 * <p>
	 * NOTE: if this method is unable to contact the API to obtain the exchange
	 * rate, it will simply do nothing and log the failure.
	 * 
	 * @param order The order to match up.
	 * 
	 */
	@Override
	public void tryToMatch(Order order) {
		try {
			double exchangeRate = ExchangeRate.getRateForPair(order.getBaseCurrency().toString(),
					order.getTargetCurrency().toString());

			List<Order> orders = getOutstandingOrders();
			for (Order orderFound : orders) {
				if (order.matches(orderFound)) {
					// We've found a match!
					// Determine how much currency to exchange.

					double firstUserHas = order.getQuantity(); // Expressed in first user's base currency.
					double firstUserWants = order.getQuantity() * exchangeRate; // Expressed in first user's target
																				// currency.

					double secondUserHas = orderFound.getQuantity(); // Expressed in second user's base currency.
					double secondUserWants = orderFound.getQuantity() / exchangeRate; // Expressed in second user's
																						// target currency.

					if (firstUserWants < secondUserHas) {
						// First user gets everything they want. Second user gets everything the first
						// user has.
						// Consume the entire order, and consume part of the orderFound.
						order.setQuantity(0);
						order.setOrderStatus(OrderStatus.COMPLETE);
						orderFound.setQuantity(secondUserHas - firstUserWants);
						orderFound.setOrderStatus(OrderStatus.PARTIALLY_COMPLETE);
						// Record the changes in a transaction object.
						Transaction trans = new Transaction(order, orderFound, order.getBaseCurrency(),
								orderFound.getBaseCurrency(), firstUserHas, firstUserWants);
						// If it's a spot order, move the money right away.
						if (order.getOrderType() == OrderType.SPOT) {
							psi.increaseCurrency(order.getTargetCurrency(), firstUserWants,
									order.getUser().getPortfolio().getPortfolioId());
							psi.increaseCurrency(orderFound.getTargetCurrency(), firstUserHas,
									orderFound.getUser().getPortfolio().getPortfolioId());
							trans.setStatus(TransactionStatus.SPOT);
						}
						// If it's a forward order, don't move any money yet, but mark the Transaction
						// appropriately.
						if (order.getOrderType() == OrderType.FORWARD) {
							trans.setScheduledDate(order.getScheduledDate());
							trans.setStatus(TransactionStatus.FORWARD_PENDING);
						}
						transRepo.save(trans);

					} else if (firstUserWants > secondUserHas) {
						// Second user gets everything they want. First user gets everything the second
						// user has.
						// Consume the entire orderFound, and consume part of the order.
						order.setQuantity(firstUserHas - secondUserWants);
						order.setOrderStatus(OrderStatus.PARTIALLY_COMPLETE);
						orderFound.setQuantity(0);
						orderFound.setOrderStatus(OrderStatus.COMPLETE);
						// Record the changes in a transaction object.
						Transaction trans = new Transaction(order, orderFound, order.getBaseCurrency(),
								orderFound.getBaseCurrency(), secondUserWants, secondUserHas);
						// If it's a spot order, move the money right away.
						if (order.getOrderType() == OrderType.SPOT) {
							psi.increaseCurrency(order.getTargetCurrency(), secondUserHas,
									order.getUser().getPortfolio().getPortfolioId());
							psi.increaseCurrency(orderFound.getTargetCurrency(), secondUserWants,
									orderFound.getUser().getPortfolio().getPortfolioId());
							trans.setStatus(TransactionStatus.SPOT);
						}
						// If it's a forward order, don't move any money yet, but mark the Transaction
						// appropriately.
						if (order.getOrderType() == OrderType.FORWARD) {
							trans.setScheduledDate(order.getScheduledDate());
							trans.setStatus(TransactionStatus.FORWARD_PENDING);
						}
						transRepo.save(trans);

					} else if (firstUserWants == secondUserHas) {
						// Both users get everything they want.
						// Consume both.
						order.setQuantity(0);
						order.setOrderStatus(OrderStatus.COMPLETE);
						orderFound.setQuantity(0);
						orderFound.setOrderStatus(OrderStatus.COMPLETE);
						// Record the changes in a transaction object.
						Transaction trans = new Transaction(order, orderFound, order.getBaseCurrency(),
								orderFound.getBaseCurrency(), firstUserHas, secondUserHas);
						// If it's a spot order, move the money right away.
						if (order.getOrderType() == OrderType.SPOT) {
							psi.increaseCurrency(order.getTargetCurrency(), firstUserWants,
									order.getUser().getPortfolio().getPortfolioId());
							psi.increaseCurrency(orderFound.getTargetCurrency(), secondUserWants,
									orderFound.getUser().getPortfolio().getPortfolioId());
							trans.setStatus(TransactionStatus.SPOT);
						}
						// If it's a forward order, don't move any money yet, but mark the Transaction
						// appropriately.
						if (order.getOrderType() == OrderType.FORWARD) {
							trans.setScheduledDate(order.getScheduledDate());
							trans.setStatus(TransactionStatus.FORWARD_PENDING);
						}
						transRepo.save(trans);
					}
					// Save changes.
					orderRepo.save(order);
					orderRepo.save(orderFound);
					// TODO logging
				}
			}
			// No match found. Do nothing.
			// TODO logging
		} catch (IOException e) {
			// Couldn't contact API. Do nothing.
			// TODO logging
		}
	}

	/**
	 * Checks all outstanding orders and expires any whose expiry dates are in the
	 * past. If the business day has ended, then this method also expires any orders
	 * whose expiry dates are today. This method does not consider time zones - all
	 * times are assumed to be server time.
	 */
	@Override
	public void expireAll() {
		LocalDate today = LocalDate.now();
		LocalTime now = LocalTime.now();
		LocalTime endOfDay = LocalTime.of(17, 0); // 17:00 = 5pm
		List<Order> list = getOutstandingOrders();
		for (Order order : list) {
			if (order.getExpiryDate().isBefore(today)
					|| (now.isAfter(endOfDay) && order.getExpiryDate().isEqual(today))) {
				expireOrder(order);
			}
		}
	}

}

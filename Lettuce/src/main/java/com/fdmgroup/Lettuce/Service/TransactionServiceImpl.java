package com.fdmgroup.Lettuce.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.Transaction;
import com.fdmgroup.Lettuce.Models.TransactionStatus;
import com.fdmgroup.Lettuce.Repo.TransactionRepo;

@Service
public class TransactionServiceImpl implements iTransaction {

	@Autowired
	TransactionRepo repo;

	@Autowired
	PortfolioServiceImpl psi;

	@Override
	public List<Transaction> getAllTransactions() {
		return repo.findAll();
	}

	@Override
	public List<Transaction> getTransactionsForOrder(Order order) {
		return repo.findByOrder(order);
	}

	@Override
	public Transaction getTransactionById(int id) {
		return repo.getById(id);
	}

	/**
	 * Resolves a single forward order, transferring the money held in it into the
	 * appropriate users' portfolios.
	 * <p>
	 * This method should only be used for transactions with
	 * TransactionStatus.FORWARD_PENDING. If called on any other type of
	 * transaction, undesired behaviour may result.
	 * 
	 * @param transaction The Transaction object which records the pending money
	 *                    transfer.
	 */
	@Override
	public void resolvePendingForwardOrder(Transaction transaction) {
		// portfolio1 sells quantity1 of currency1, so they receive quantity2 of
		// currency2
		// portfolio2 sells quantity2 of currency2, so they receive quantity1 of
		// currency1
		psi.increaseCurrency(transaction.getCurrency2(), transaction.getQuantity2(),
				transaction.getOrder1().getUser().getPortfolio().getPortfolioId());
		psi.increaseCurrency(transaction.getCurrency1(), transaction.getQuantity1(),
				transaction.getOrder2().getUser().getPortfolio().getPortfolioId());
		transaction.setStatus(TransactionStatus.FORWARD_COMPLETE);
	}

	/**
	 * Checks all transactions relating to pending forward orders, and resolves any
	 * whose scheduled dates are in the past. If the business day has ended, then it
	 * also resolves any pending forward orders whose scheduled dates are today.
	 * This method does not consider time zones - all times are assumed to be server
	 * time.
	 */
	@Override
	public void resolveAllPending() {
		LocalDate today = LocalDate.now();
		LocalTime now = LocalTime.now();
		LocalTime endOfDay = LocalTime.of(17, 0); // 17:00 = 5pm
		List<Transaction> list = repo.findByStatus(TransactionStatus.FORWARD_PENDING);
		for (Transaction trans : list) {
			if (trans.getScheduledDate().isBefore(today)
					|| (now.isAfter(endOfDay) && trans.getScheduledDate().isEqual(today))) {
				resolvePendingForwardOrder(trans);
			}
		}
	}

}

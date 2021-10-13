package com.fdmgroup.Lettuce.Service;

import java.util.List;

import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.Transaction;

public interface iTransaction {
	
	public List<Transaction> getAllTransactions();
	public List<Transaction> getTransactionsForOrder(Order order);
	public Transaction getTransactionById(int id);
	
	public void resolvePendingForwardOrder(Transaction transaction);
	public void resolveAllPending();

}

package com.fdmgroup.Lettuce.Service;

import java.util.List;

import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.Transaction;

public interface iTransaction {
	
	List<Transaction> getAllTransactions();
	List<Transaction> getTransactionsForOrder(Order order);
	Transaction getTransactionById(int id);

}

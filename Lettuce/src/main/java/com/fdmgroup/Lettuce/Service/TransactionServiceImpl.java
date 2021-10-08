package com.fdmgroup.Lettuce.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.Transaction;
import com.fdmgroup.Lettuce.Repo.TransactionRepo;

public class TransactionServiceImpl implements iTransaction {
	
	@Autowired
	TransactionRepo repo;

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

}

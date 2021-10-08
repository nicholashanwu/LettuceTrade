package com.fdmgroup.Lettuce.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.Transaction;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Integer> {
	
	@Query("select t from Transaction t where t.order1 = ?1 or t.order2 = ?1")
	List<Transaction> findByOrder(Order order);

}

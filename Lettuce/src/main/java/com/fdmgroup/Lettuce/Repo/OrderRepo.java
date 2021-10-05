package com.fdmgroup.Lettuce.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.OrderType;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
	String q1 = "select o from Order o where o.orderType=?1";
	@Query(q1)
	List<Order> orderByType(OrderType ot);

}

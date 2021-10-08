package com.fdmgroup.Lettuce.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.OrderStatus;
import com.fdmgroup.Lettuce.Models.OrderType;
import com.fdmgroup.Lettuce.Models.User;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
	@Query("select o from Order o where o.orderType=?1")
	List<Order> orderByType(OrderType ot);
	
	@Query("select o from Order o where o.orderStatus=?1")
	List<Order> orderByStatus(OrderStatus os);
	
	@Query("select o from Order o where o.user=?1")
	List<Order> orderByUser(User user);

}

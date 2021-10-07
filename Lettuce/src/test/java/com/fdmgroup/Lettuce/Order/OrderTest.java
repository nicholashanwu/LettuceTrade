package com.fdmgroup.Lettuce.Order;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.OrderType;
import com.fdmgroup.Lettuce.Models.User;
import com.fdmgroup.Lettuce.Repo.OrderRepo;
import com.fdmgroup.Lettuce.Repo.UserRepo;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderTest {
	@Autowired
	OrderRepo or;
	@Autowired
	UserRepo ur;
	
	@Test
	public void initially_return_empty_orderDB() {
		Iterable<Order> orders = or.findAll();
		assertThat(orders).isEmpty();
	}
	
	@Test
	public void saves_item_in_orderDB() {
		User u = ur.save(new User());
		Order o = or.save(new Order(OrderType.SPOT, 1.2, 100.2, LocalDate.now(), LocalDate.now(), u));
		System.out.println(o);
		assertThat(o).hasFieldOrPropertyWithValue("orderType", OrderType.SPOT);
		assertThat(o).hasFieldOrPropertyWithValue("ppu", 1.2);
		assertThat(o).hasFieldOrPropertyWithValue("quantity", 100.2);
		assertThat(o).hasFieldOrPropertyWithValue("expiryDate", LocalDate.now());
		assertThat(o).hasFieldOrPropertyWithValue("scheduledDate", LocalDate.now());
		assertThat(o).hasFieldOrPropertyWithValue("user", u);
	}
	@Test
	public void find_orders_of_type_SPOT() {
		User u = ur.save(new User());
		Order o1 = or.save(new Order(OrderType.SPOT, 1.2, 100.2, LocalDate.now(), LocalDate.now(), u));

		Order o2 = or.save(new Order(OrderType.FORWARD, 100.2, 1.2, LocalDate.of(2015, 12, 12), LocalDate.of(2015, 12, 12), u));

		Order o3 = or.save(new Order(OrderType.FORWARD, 1.5, 10.2, LocalDate.of(2017, 12, 12), LocalDate.of(2017, 12, 12), u));

		Iterable<Order> items = or.orderByType(OrderType.SPOT);
		
		assertThat(items).hasSize(1).contains(o1);

	}
	@Test
	public void update_order_by_id() {
		User u = ur.save(new User());
		Order o1 = or.save(new Order(OrderType.SPOT, 1.2, 100.2, LocalDate.now(), LocalDate.now(), u));

		Order newValues = or.save(new Order(OrderType.FORWARD, 100.2, 1.2, LocalDate.of(2015, 12, 12), LocalDate.of(2015, 12, 12),u));
		
		Order o = or.findById(o1.getOrderId()).get();
		o.setOrderType(newValues.getOrderType());
		o.setPpu(newValues.getPpu());
		o.setQuantity(newValues.getQuantity());
		o.setExpiryDate(newValues.getExpiryDate());
		o.setScheduledDate(newValues.getScheduledDate());
		o.setUser(newValues.getUser());
		or.save(o);
		
		Order checkOrder = or.findById(o1.getOrderId()).get();
		
		assertThat(checkOrder.getOrderId()).isEqualTo(o1.getOrderId()); //Can't compare to newValues item as it will definetly have a diff ID.
		assertThat(checkOrder.getOrderType()).isEqualTo(newValues.getOrderType());
		assertThat(checkOrder.getPpu()).isEqualTo(newValues.getPpu());
		assertThat(checkOrder.getQuantity()).isEqualTo(newValues.getQuantity());
		assertThat(checkOrder.getExpiryDate()).isEqualTo(newValues.getExpiryDate());
		assertThat(checkOrder.getScheduledDate()).isEqualTo(newValues.getScheduledDate());
		assertThat(checkOrder.getUser()).isEqualTo(newValues.getUser());
	}
	@Test
	public void delete_Order_by_id() {
		User u = ur.save(new User());
		Order o1 = or.save(new Order(OrderType.SPOT, 1.2, 100.2, LocalDate.now(), LocalDate.now(), u));

		Order o2 = or.save(new Order(OrderType.FORWARD, 100.2, 1.2, LocalDate.of(2015, 12, 12), LocalDate.of(2015, 12, 12), u));

		Order o3 = or.save(new Order(OrderType.FORWARD, 1.5, 10.2, LocalDate.of(2017, 12, 12), LocalDate.of(2017, 12, 12), u));
		
		or.deleteById(o3.getOrderId());
		
		Iterable<Order> orders = or.findAll();
		
		assertThat(orders).hasSize(2).contains(o1, o2);
		
		
	}

}

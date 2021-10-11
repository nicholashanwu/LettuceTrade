package com.fdmgroup.Lettuce;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.OrderType;
import com.fdmgroup.Lettuce.Models.User;
import com.fdmgroup.Lettuce.Repo.CurrencyRepo;
import com.fdmgroup.Lettuce.Repo.OrderRepo;
import com.fdmgroup.Lettuce.Repo.UserRepo;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderRepoTest {
	@Autowired
	OrderRepo or;
	@Autowired
	UserRepo ur;
	@Autowired
	CurrencyRepo cr;
	
	LocalDate now;
	Currency usd;
	Currency eur;
	User u;
	
	@BeforeEach
	public void setup() {
		now = LocalDate.now();
		usd = cr.save(new Currency("USD"));
		eur = cr.save(new Currency("EUR"));
		u = ur.save(new User());
	}
	
	@Test
	public void initially_return_empty_orderDB() {
		Iterable<Order> orders = or.findAll();
		assertThat(orders).isEmpty();
	}
	
	@Test
	public void saves_item_in_orderDB() {
		Order o = or.save(new Order(u, usd, eur, OrderType.SPOT, 100.2, now));
		
		assertThat(o).hasFieldOrPropertyWithValue("user", u);
		assertThat(o).hasFieldOrPropertyWithValue("baseCurrency", usd);
		assertThat(o).hasFieldOrPropertyWithValue("targetCurrency", eur);
		assertThat(o).hasFieldOrPropertyWithValue("orderType", OrderType.SPOT);
		assertThat(o).hasFieldOrPropertyWithValue("quantity", 100.2);
		assertThat(o).hasFieldOrPropertyWithValue("expiryDate", now);
	}
	@Test
	public void find_orders_of_type_SPOT() {
		Order o1 = or.save(new Order(u, usd, eur, OrderType.SPOT, 100.2, now));
		Order o2 = or.save(new Order(u, usd, eur, OrderType.FORWARD, 100.2, now));
		Order o3 = or.save(new Order(u, usd, eur, OrderType.FORWARD, 1.5, now));

		Iterable<Order> items = or.getByType(OrderType.SPOT);
		
		assertThat(items).hasSize(1).contains(o1);
	}
	@Test
	public void update_order_by_id() {
		Order o1 = or.save(new Order(u, usd, eur, OrderType.SPOT, 100.2, now));
		Order newValues = new Order(u, usd, eur, OrderType.FORWARD, 100.2, now);
		
		Order o = or.findById(o1.getOrderId()).get();
		o.setOrderType(newValues.getOrderType());
		o.setQuantity(newValues.getQuantity());
		o.setExpiryDate(newValues.getExpiryDate());
		o.setUser(newValues.getUser());
		or.save(o);
		
		Order checkOrder = or.findById(o1.getOrderId()).get();
		
		assertThat(checkOrder.getOrderId()).isEqualTo(o1.getOrderId()); //Can't compare to newValues item as it will definetly have a diff ID.
		assertThat(checkOrder.getOrderType()).isEqualTo(newValues.getOrderType());
		assertThat(checkOrder.getQuantity()).isEqualTo(newValues.getQuantity());
		assertThat(checkOrder.getExpiryDate()).isEqualTo(newValues.getExpiryDate());
		assertThat(checkOrder.getUser()).isEqualTo(newValues.getUser());
	}
	@Test
	public void delete_Order_by_id() {
		Order o1 = or.save(new Order(u, usd, eur, OrderType.SPOT, 100.2, now));
		Order o2 = or.save(new Order(u, usd, eur, OrderType.FORWARD, 100.2, now));
		Order o3 = or.save(new Order(u, usd, eur, OrderType.FORWARD, 1.5, now));
		
		or.deleteById(o3.getOrderId());
		
		Iterable<Order> orders = or.findAll();
		
		assertThat(orders).hasSize(2).contains(o1, o2);
		
		
	}

}

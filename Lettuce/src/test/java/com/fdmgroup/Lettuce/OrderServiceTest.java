package com.fdmgroup.Lettuce;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.OrderStatus;
import com.fdmgroup.Lettuce.Models.Transaction;
import com.fdmgroup.Lettuce.Repo.OrderRepo;
import com.fdmgroup.Lettuce.Repo.TransactionRepo;
import com.fdmgroup.Lettuce.Service.OrderServiceImpl;

/*
 * Beware: this is interacting with a live API (I haven't figured out how to mock that yet),
 * so the return values will be different on different days. The following tests are written
 * assuming that ExchangeRate.getRateForPair("USD", "EUR") will return 0.8631, which was true
 * on the day I wrote it.
 */

@RunWith(SpringRunner.class)
public class OrderServiceTest {

	@Mock
	OrderRepo orderRepo;

	@Mock
	TransactionRepo transRepo;

	@InjectMocks
	OrderServiceImpl service;

	@Captor
	ArgumentCaptor<Transaction> transactionCaptor;

	Currency usd = new Currency("USD");
	Currency eur = new Currency("EUR");
	Order o1 = new Order();
	Order o2 = new Order();
	List<Order> list = new ArrayList<>();;

	/*
	 * In this test, o1 and o2 don't match (both trades are trying to go USD->EUR so
	 * they can't resolve each other), so nothing changes. Nothing is deleted and
	 * the quantities are unchanged.
	 */
	@Test
	public void should_not_find_match() {
		// Arrange
		o1.setOrderStatus(OrderStatus.PENDING);
		o1.setBaseCurrency(usd);
		o1.setTargetCurrency(eur);
		o1.setQuantity(50.0);

		o2.setOrderStatus(OrderStatus.PENDING);
		o2.setBaseCurrency(usd);
		o2.setTargetCurrency(eur);
		o2.setQuantity(50.0);

		// Stub the repo's return value
		list.add(o1);
		list.add(o2);
		when(orderRepo.findAll()).thenReturn(list);
		when(orderRepo.orderByStatus(OrderStatus.PENDING)).thenReturn(list);

		// Act
		service.tryToMatch(o1);

		// Assert
		assertThat(o1.getOrderStatus()).isEqualTo(OrderStatus.PENDING);
		assertThat(o2.getOrderStatus()).isEqualTo(OrderStatus.PENDING);
		assertThat(o1.getQuantity()).isCloseTo(50.0, within(0.1));
		assertThat(o2.getQuantity()).isCloseTo(50.0, within(0.1));
	}

	/*
	 * In this test, o1 has a matching order: o2, which has the same currencies in
	 * reverse. o2's quantity is larger. So o1 is deleted and the appropriate amount
	 * is subtracted from o2.
	 */
	@Test
	public void should_find_match_where_o2_is_larger() {
		// Arrange
		o1.setOrderStatus(OrderStatus.PENDING);
		o1.setBaseCurrency(usd);
		o1.setTargetCurrency(eur);
		o1.setQuantity(50.0); // USD 50.0 is worth EUR 43.155

		o2.setOrderStatus(OrderStatus.PENDING);
		o2.setBaseCurrency(eur);
		o2.setTargetCurrency(usd);
		o2.setQuantity(50.0);

		// Stub the repo's return value
		list.add(o1);
		list.add(o2);
		when(orderRepo.findAll()).thenReturn(list);
		when(orderRepo.orderByStatus(OrderStatus.PENDING)).thenReturn(list);

		// Act
		service.tryToMatch(o1);

		// Assert
		assertThat(o1.getOrderStatus()).isEqualTo(OrderStatus.COMPLETE);
		assertThat(o2.getOrderStatus()).isEqualTo(OrderStatus.PARTIALLY_COMPLETE);
		assertThat(o1.getQuantity()).isCloseTo(0.0, within(0.1));
		assertThat(o2.getQuantity()).isCloseTo(6.845, within(0.1));
	}

	/*
	 * This test essentially repeats the previous one, except that this time o1 is
	 * larger, to ensure we don't have any symmetry errors.
	 */
	@Test
	public void should_find_match_where_o1_is_larger() {
		// Arrange
		o1.setOrderStatus(OrderStatus.PENDING);
		o1.setBaseCurrency(usd);
		o1.setTargetCurrency(eur);
		o1.setQuantity(50.0);

		o2.setOrderStatus(OrderStatus.PENDING);
		o2.setBaseCurrency(eur);
		o2.setTargetCurrency(usd);
		o2.setQuantity(30.0); // EUR 30.0 is worth USD 34.7584

		// Stub the repo's return value
		list.add(o1);
		list.add(o2);
		when(orderRepo.findAll()).thenReturn(list);
		when(orderRepo.orderByStatus(OrderStatus.PENDING)).thenReturn(list);

		// Act
		service.tryToMatch(o1);

		// Assert
		assertThat(o1.getOrderStatus()).isEqualTo(OrderStatus.PARTIALLY_COMPLETE);
		assertThat(o2.getOrderStatus()).isEqualTo(OrderStatus.COMPLETE);
		assertThat(o1.getQuantity()).isCloseTo(15.2416, within(0.1));
		assertThat(o2.getQuantity()).isCloseTo(0.0, within(0.1));
	}

	/*
	 * This test essentially repeats the previous two, except that this time both
	 * orders are the same size, so they should both be deleted.
	 */
	@Test
	public void should_find_match_equal_quantities() {
		// Arrange
		o1.setOrderStatus(OrderStatus.PENDING);
		o1.setBaseCurrency(usd);
		o1.setTargetCurrency(eur);
		o1.setQuantity(50.0);

		o2.setOrderStatus(OrderStatus.PENDING);
		o2.setBaseCurrency(eur);
		o2.setTargetCurrency(usd);
		o2.setQuantity(43.155);

		// Stub the repo's return value
		list.add(o1);
		list.add(o2);
		when(orderRepo.findAll()).thenReturn(list);
		when(orderRepo.orderByStatus(OrderStatus.PENDING)).thenReturn(list);

		// Act
		service.tryToMatch(o1);

		// Assert
		assertThat(o1.getOrderStatus()).isEqualTo(OrderStatus.COMPLETE);
		assertThat(o2.getOrderStatus()).isEqualTo(OrderStatus.COMPLETE);
		assertThat(o1.getQuantity()).isCloseTo(0.0, within(0.1));
		assertThat(o2.getQuantity()).isCloseTo(0.0, within(0.1));
	}

	/*
	 * In this test, we check whether the service records the history of the
	 * transaction in a pair of CompletedOrder objects.
	 */
	@Test
	public void should_save_completed_order() {
		// Arrange
		o1.setOrderStatus(OrderStatus.PENDING);
		o1.setBaseCurrency(usd);
		o1.setTargetCurrency(eur);
		o1.setQuantity(50.0); // USD 50.0 is worth EUR 43.155

		o2.setOrderStatus(OrderStatus.PENDING);
		o2.setBaseCurrency(eur);
		o2.setTargetCurrency(usd);
		o2.setQuantity(50.0);

		// Stub the repo's return value
		list.add(o1);
		list.add(o2);
		when(orderRepo.findAll()).thenReturn(list);
		when(orderRepo.orderByStatus(OrderStatus.PENDING)).thenReturn(list);

		// Act
		service.tryToMatch(o1);

		// Assert
		verify(transRepo, times(1)).save(transactionCaptor.capture());
		Transaction createdTransaction = transactionCaptor.getValue();
		assertThat(createdTransaction.getCurrency1()).isEqualTo(usd);
		assertThat(createdTransaction.getCurrency2()).isEqualTo(eur);
		assertThat(createdTransaction.getOrder1()).isEqualTo(o1);
		assertThat(createdTransaction.getOrder2()).isEqualTo(o2);
		assertThat(createdTransaction.getQuantity1()).isCloseTo(50.0, within(0.1));
		assertThat(createdTransaction.getQuantity2()).isCloseTo(43.155, within(0.1));
	}

}

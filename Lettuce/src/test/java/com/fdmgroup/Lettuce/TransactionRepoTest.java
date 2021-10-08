package com.fdmgroup.Lettuce;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.Order;
import com.fdmgroup.Lettuce.Models.Transaction;
import com.fdmgroup.Lettuce.Repo.CurrencyRepo;
import com.fdmgroup.Lettuce.Repo.OrderRepo;
import com.fdmgroup.Lettuce.Repo.TransactionRepo;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TransactionRepoTest {
	@Autowired
	OrderRepo orderRepo;
	@Autowired
	CurrencyRepo currRepo;
	@Autowired
	TransactionRepo transRepo;
	
	Order o1;
	Order o2;
	Order o3;
	Order o4;
	Currency usd;
	Currency eur;
	
	@Test
	public void find_all() {
		// Arrange
		o1 = orderRepo.save(new Order());
		o2 = orderRepo.save(new Order());
		o3 = orderRepo.save(new Order());
		o4 = orderRepo.save(new Order());
		usd = currRepo.save(new Currency("USD"));
		eur = currRepo.save(new Currency("EUR"));
		Transaction t1 = transRepo.save(new Transaction(o1, o2, usd, eur, 0, 0));
		Transaction t2 = transRepo.save(new Transaction(o1, o3, usd, eur, 0, 0));
		Transaction t3 = transRepo.save(new Transaction(o3, o4, usd, eur, 0, 0));
		Transaction t4 = transRepo.save(new Transaction(o2, o4, usd, eur, 0, 0));
		
		// Act
		List<Transaction> checkList = transRepo.findAll();
		
		// Assert
		assertThat(checkList).hasSize(4);
	}
	
	@Test
	public void find_by_order() {
		// Arrange
		o1 = orderRepo.save(new Order());
		o2 = orderRepo.save(new Order());
		o3 = orderRepo.save(new Order());
		o4 = orderRepo.save(new Order());
		usd = currRepo.save(new Currency("USD"));
		eur = currRepo.save(new Currency("EUR"));
		Transaction t1 = transRepo.save(new Transaction(o1, o2, usd, eur, 0, 0));
		Transaction t2 = transRepo.save(new Transaction(o1, o3, usd, eur, 0, 0));
		Transaction t3 = transRepo.save(new Transaction(o3, o4, usd, eur, 0, 0));
		Transaction t4 = transRepo.save(new Transaction(o2, o4, usd, eur, 0, 0));
		
		// Act
		List<Transaction> checkList = transRepo.findByOrder(o1);
		
		// Assert
		assertThat(checkList).hasSize(2).contains(t1, t2);
	}
	
	@Test
	public void find_by_order_mixed_position() {
		// Arrange
		o1 = orderRepo.save(new Order());
		o2 = orderRepo.save(new Order());
		o3 = orderRepo.save(new Order());
		o4 = orderRepo.save(new Order());
		usd = currRepo.save(new Currency("USD"));
		eur = currRepo.save(new Currency("EUR"));
		Transaction t1 = transRepo.save(new Transaction(o1, o2, usd, eur, 0, 0));
		Transaction t2 = transRepo.save(new Transaction(o1, o3, usd, eur, 0, 0));
		Transaction t3 = transRepo.save(new Transaction(o3, o4, usd, eur, 0, 0));
		Transaction t4 = transRepo.save(new Transaction(o2, o4, usd, eur, 0, 0));
		
		// Act
		List<Transaction> checkList = transRepo.findByOrder(o3);
		
		// Assert
		assertThat(checkList).hasSize(2).contains(t2, t3);
	}

}

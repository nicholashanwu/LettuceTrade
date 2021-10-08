package com.fdmgroup.Lettuce;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.fdmgroup.Lettuce.Models.User;
import com.fdmgroup.Lettuce.Repo.UserRepo;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserDBTesting {
	@Autowired
	private TestEntityManager em;
	
	@Autowired
	UserRepo userRepo;
	
	@Test
	public void should_find_no_currency_if_repository_is_empty() {
		Iterable<User> Users = userRepo.findAll();
		assertThat(Users).isEmpty();
	}
	
	@Test
	public void should_store_a_User() {
		User user1 = new User("email1","firstName1", "lastName1","password1", 100.0,"True");
		userRepo.save(user1);
		assertThat(user1).hasFieldOrPropertyWithValue("email", "email1");
		assertThat(user1).hasFieldOrPropertyWithValue("firstName", "firstName1");
		assertThat(user1).hasFieldOrPropertyWithValue("lastName", "lastName1");
		assertThat(user1).hasFieldOrPropertyWithValue("password", "password1");
		assertThat(user1).hasFieldOrPropertyWithValue("bankAccountBalance", 100.0);
		assertThat(user1).hasFieldOrPropertyWithValue("admin", "True");

	}
	
	@Test
	public void should_find_all_User() {
		User user1 = new User( "email1","firstName1", "lastName1","password1", 100.0,"True");
		userRepo.save(user1);

		User user2 = new User( "email2","firstName2", "lastName2","password2", 100.0,"false");
		userRepo.save(user2);

		User user3 = new User( "email3","firstName3", "lastName3","password3", 1500.0,"true");
		userRepo.save(user3);

		Iterable<User> userList = userRepo.findAll();

		assertThat(userList).hasSize(3).contains(user1,user2,user3);
	}
	@Test
	public void should_find_user_by_userId() {
		
		User user1 = new User( "email1","firstName1", "lastName1","password1", 100.0,"true");
		userRepo.save(user1);
		User user2 = new User( "email2","firstName2", "lastName2","password2", 100.0,"false");
		userRepo.save(user2);
		
		User findUser = userRepo.findById(user1.getUserId()).get();
		
		assertThat(findUser).isEqualTo(user1);
	}
	
	@Test
	public void find_user_by_email() {
		User user1 = new User( "email1","firstName1", "lastName1","password1", 100.0,"true");
		userRepo.save(user1);
		User user2 = new User( "email2","firstName2", "lastName2","password2", 100.0,"false");
		userRepo.save(user2);
		User findUser = userRepo.getByEmail("email1").get();
		assertThat(findUser).isEqualTo(user1);
	}
	
	@Test
	public void find_user_by_emailandPassword() {
		User user1 = new User( "email1","firstName1", "lastName1","password1", 100.0,"true");
		userRepo.save(user1);
		User user2 = new User( "email2","firstName2", "lastName2","password2", 100.0,"false");
		userRepo.save(user2);
		User findUser = userRepo.getUserByEmailAndPassword("email1","password1").get();
		assertThat(findUser).isEqualTo(user1);
	}
	@Test
	public void should_update_user_by_id() {
		User user1 = new User( "email1","firstName1", "lastName1","password1", 100.0,"true");
		userRepo.save(user1);
		User updateUser = new User( "email2","firstName2", "lastName2","password2", 100.0,"false");
		
		User user = userRepo.findById(user1.getUserId()).get();
		user.setEmail(updateUser.getEmail());
		user.setFirstName(updateUser.getFirstName());
		user.setLastName(updateUser.getLastName());
		user.setPassword(updateUser.getPassword());
		userRepo.save(user);
		
		User checkUser = userRepo.findById(user1.getUserId()).get();
		assertThat(checkUser.getEmail()).isEqualTo(updateUser.getEmail());
		assertThat(checkUser.getFirstName()).isEqualTo(updateUser.getFirstName());
		assertThat(checkUser.getLastName()).isEqualTo(updateUser.getLastName());
		
	}
	
	@Test 
	public void should_not_have_two_users_with_the_same_email() {
		User user1 = new User( "email1","firstName1", "lastName1","password1", 100.0,"true");
		userRepo.save(user1);
		User user2 = new User( "email1","firstName2", "lastName2","password2", 100.0,"false");
		userRepo.save(user2);
		boolean exception = false;
		try {
			Optional<User> user =userRepo.getByEmail("username1");

		}catch (DataIntegrityViolationException e) {
			exception=true;
		}
		assertThat(exception).isEqualTo(true);
		
	}
	
}

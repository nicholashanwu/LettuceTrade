package com.fdmgroup.Lettuce.Repo;

import org.springframework.stereotype.Repository;

import com.fdmgroup.Lettuce.Models.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


@Repository
public interface UserRepo extends JpaRepository<User, Integer>{

	String getByEmailQ = "select u from User u where u.email=?1";
	String getByUserNameAndPasswordQ = "select u from User u where u.email=?1 and u.password=?2";
	
	@Query(getByEmailQ)
	Optional<User> getByEmail(String email);
	
	@Query(getByUserNameAndPasswordQ)
	Optional<User> getUserByEmailAndPassword(String email, String password);
}


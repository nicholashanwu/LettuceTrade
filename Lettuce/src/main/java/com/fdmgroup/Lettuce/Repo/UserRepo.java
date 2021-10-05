package com.fdmgroup.Lettuce.Repo;

import org.springframework.stereotype.Repository;

import com.fdmgroup.Lettuce.Models.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


@Repository
public interface UserRepo extends JpaRepository<User, Integer>{

	String getByUserNameQ = "select u from User u where u.userName=?1";
	String getByUserNameAndPasswordQ = "select u from User u where u.userName=?1 and u.password=?2";
	
	@Query(getByUserNameQ)
	Optional<User> getByUserName(String userName);
	
	@Query(getByUserNameAndPasswordQ)
	Optional<User> getUserByUserNameAndPassword(String userName, String password);
}


package com.fdmgroup.Lettuce.Repo;

import org.springframework.stereotype.Repository;

import com.fdmgroup.Lettuce.Models.Portfolio;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PortfolioRepo extends JpaRepository<Portfolio, Integer> {
	
}

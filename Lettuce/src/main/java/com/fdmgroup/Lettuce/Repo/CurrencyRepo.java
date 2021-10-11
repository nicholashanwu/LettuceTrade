package com.fdmgroup.Lettuce.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.Lettuce.Models.Currency;

public interface CurrencyRepo extends JpaRepository<Currency,String> {

}

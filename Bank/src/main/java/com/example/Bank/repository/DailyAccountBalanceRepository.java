package com.example.Bank.repository;

import org.springframework.data.repository.Repository;

import com.example.Bank.model.DailyAccountBalance;

public interface DailyAccountBalanceRepository extends Repository<DailyAccountBalance, Long>{

	public DailyAccountBalance save(DailyAccountBalance entity);
	
	public DailyAccountBalance findOne(Long id);
}

package com.example.Bank.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.example.Bank.model.Account;

public interface AccountRepository extends Repository<Account, Long> {

	public Account save(Account entity);
	
	public List<Account> findByAccountNumber(String accountNumber);
}

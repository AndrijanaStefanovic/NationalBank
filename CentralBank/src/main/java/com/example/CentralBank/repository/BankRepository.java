package com.example.CentralBank.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.example.CentralBank.model.Bank;

public interface BankRepository extends Repository<Bank, Long>{

	public List<Bank> findBySwiftCode(String swiftCode);
	
	public Bank save(Bank entity);
}

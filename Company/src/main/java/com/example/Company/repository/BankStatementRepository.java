package com.example.Company.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import com.example.Company.model.BankStatement;

public interface BankStatementRepository extends Repository<BankStatement, Long> {
	
	public BankStatement save(BankStatement bankStatement);
	
	public BankStatement findOne(long id);
	
	public Page<BankStatement> findAll(Pageable pageable);
	
}

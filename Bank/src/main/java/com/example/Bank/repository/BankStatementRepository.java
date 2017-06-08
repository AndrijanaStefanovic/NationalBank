package com.example.Bank.repository;

import org.springframework.data.repository.Repository;

import com.example.Bank.model.BankStatementModel;

public interface BankStatementRepository extends Repository<BankStatementModel, Long>{

	public BankStatementModel save(BankStatementModel entity);
}

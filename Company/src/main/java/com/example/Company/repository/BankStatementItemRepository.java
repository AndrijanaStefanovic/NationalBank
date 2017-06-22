package com.example.Company.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import com.example.Company.model.BankStatement;
import com.example.Company.model.BankStatementItem;

public interface BankStatementItemRepository extends Repository<BankStatementItem, Long> {
	
	public BankStatementItem save(BankStatementItem bankStatementItem);
	
	public BankStatementItem findOne(long id);
	
	public List<BankStatementItem> findByBankStatement(BankStatement bankStatement);
	
	public Page<BankStatementItem> findAll(Pageable pageable);

}

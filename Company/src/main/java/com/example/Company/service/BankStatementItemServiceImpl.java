package com.example.Company.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Company.model.BankStatement;
import com.example.Company.model.BankStatementItem;
import com.example.Company.repository.BankStatementItemRepository;
import com.example.Company.repository.BankStatementRepository;

@Service
public class BankStatementItemServiceImpl implements BankStatementItemService {
	
	@Autowired
	private BankStatementItemRepository bankStatementItemRepository;
	
	@Autowired
	private BankStatementRepository bankStatementRepository;

	@Override
	public String createBankStatementItem(BankStatementItem bankStatementItem) {
		bankStatementItemRepository.save(bankStatementItem);
		return "200";
	}

	@Override
	public Collection<BankStatementItem> getBankStatementItemsByBankStatement(Long id) {
		BankStatement bankStatement = bankStatementRepository.findOne(id);
		return bankStatementItemRepository.findByBankStatement(bankStatement);
	}

}

package com.example.Company.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Company.model.BankStatement;
import com.example.Company.repository.BankStatementRepository;

@Service
public class BankStatementServiveImpl implements BankStatementService {
	
	@Autowired
	private BankStatementRepository bankStatementRepository;

	@Override
	public String createBankStatement(BankStatement bankStatement) {
		bankStatementRepository.save(bankStatement);
		return "200";
	}

	@Override
	public Collection<BankStatement> getAllBankStatements() {
		return bankStatementRepository.findAll(null).getContent();
	}

}

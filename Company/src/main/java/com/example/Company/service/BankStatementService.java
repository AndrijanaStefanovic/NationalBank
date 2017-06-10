package com.example.Company.service;

import java.util.Collection;

import com.example.Company.model.BankStatement;

public interface BankStatementService {

	public String createBankStatement(BankStatement bankStatement);

	public Collection<BankStatement> getAllBankStatements();
	
}

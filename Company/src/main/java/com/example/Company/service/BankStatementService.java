package com.example.Company.service;

import java.util.Collection;

import com.example.Company.model.BankStatement;
import com.example.service.bankstatementrequest.BankStatementRequest;

public interface BankStatementService {

	public String createBankStatementRequest(BankStatementRequest bankStatementRequest);

	public Collection<BankStatement> getAllBankStatements();
	
}

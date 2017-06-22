package com.example.Company.service;

import java.util.Collection;

import com.example.Company.model.BankStatement;
import com.example.Company.model.BankStatementItem;

public interface BankStatementItemService {

	public String createBankStatementItem(BankStatementItem bankStatementItem);
	
	public Collection<BankStatementItem> getBankStatementItemsByBankStatement(Long id);
	
}

package com.example.Company.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Company.model.BankStatement;
import com.example.Company.repository.BankStatementRepository;
import com.example.service.bankstatementrequest.BankStatementRequest;

@Service
public class BankStatementServiveImpl implements BankStatementService {
	
	@Autowired
	private BankStatementRepository bankStatementRepository;
	
	@Autowired
	private SOAPClientServiceImpl SOAPClientService;

	@Override
	public String createBankStatementRequest(BankStatementRequest bankStatementRequest) {
		return SOAPClientService.sendBankStatementRequest(bankStatementRequest);
	}

	@Override
	public Collection<BankStatement> getAllBankStatements() {
		return bankStatementRepository.findAll(null).getContent();
	}

}

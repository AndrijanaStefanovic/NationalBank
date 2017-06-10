package com.example.Company.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Company.model.BankStatement;
import com.example.Company.service.BankStatementService;

@RestController
public class BankStatementController {

	@Autowired
	private BankStatementService bankStatementService;
	
	@RequestMapping(
			value = "/bankStatement/create",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	public String createBankStatement(@RequestBody BankStatement bankStatement) {
		return bankStatementService.createBankStatement(bankStatement);
	}
	
	@RequestMapping(
			value = "/bankStatement/getAllBankStatements",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public Collection<BankStatement> getAllBankStatements() {
		return bankStatementService.getAllBankStatements();
	}
}

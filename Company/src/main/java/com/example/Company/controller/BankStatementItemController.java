package com.example.Company.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Company.model.BankStatementItem;
import com.example.Company.service.BankStatementItemService;

@RestController
public class BankStatementItemController {
	
	@Autowired
	private BankStatementItemService bankStatementItemService;
	
	@RequestMapping(
			value = "/bankStatementItems/create",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	public String createBankStatementItem(@RequestBody BankStatementItem bankStatementItem) {
		return bankStatementItemService.createBankStatementItem(bankStatementItem);
	}
	
	@RequestMapping(
			value = "/bankStatementItems/getAllBankStatementItems/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public Collection<BankStatementItem> getAllBankStatementItems(@PathVariable("id") Long id) {
		return bankStatementItemService.getBankStatementItemsByBankStatement(id);
	}
}

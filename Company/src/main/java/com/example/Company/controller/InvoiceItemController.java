package com.example.Company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Company.model.InvoiceItem;
import com.example.Company.service.InvoiceItemService;

@RestController
public class InvoiceItemController {

	@Autowired
	private InvoiceItemService invoiceItemService;
	
	@RequestMapping(
			value = "/invoiceItem/create",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	public String createInvoiceItem(@RequestBody InvoiceItem invoiceItem) {
		return invoiceItemService.createInvoiceItem(invoiceItem);
	}
}

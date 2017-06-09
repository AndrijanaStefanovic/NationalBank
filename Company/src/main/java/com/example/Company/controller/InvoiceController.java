package com.example.Company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Company.model.Invoice;
import com.example.Company.service.InvoiceService;

@RestController
public class InvoiceController {

	@Autowired
	private InvoiceService invoiceService;
	
	@RequestMapping(
			value = "/invoice/create",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	public String createInvoice(@RequestBody Invoice invoice) {
		return invoiceService.createInvoice(invoice);
	}
}

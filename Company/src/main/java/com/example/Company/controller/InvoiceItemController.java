package com.example.Company.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
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
			value = "/invoiceItem/create/{invoiceId}",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	public String createInvoiceItem(@PathVariable("invoiceId") Long invoiceId, @RequestBody InvoiceItem invoiceItem) {
		return invoiceItemService.createInvoiceItem(invoiceId, invoiceItem);
	}
	
	@RequestMapping(
			value = "/invoiceItem/getInvoiceItems/{invoiceId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public Collection<InvoiceItem> createInvoiceItem(@PathVariable("invoiceId") Long invoiceId) {
		return invoiceItemService.getInvoiceItems(invoiceId);
	}
	
	@RequestMapping(
			value = "/invoiceItem/delete",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	public String deleteInvoiceItem(@RequestBody Long id) {
		return invoiceItemService.deleteInvoiceItem(id);
	}
}

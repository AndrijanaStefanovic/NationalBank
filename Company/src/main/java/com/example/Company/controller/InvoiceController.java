package com.example.Company.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Company.model.Invoice;
import com.example.Company.model.pojo.PaymentOrderModel;
import com.example.Company.service.InvoiceService;
import com.example.Company.service.SOAPClientService;

@RestController
public class InvoiceController {

	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private SOAPClientService SOAPClientService;
	
	@RequestMapping(
			value = "/invoice/create",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	public String createInvoice(@RequestBody Invoice invoice) {
		return invoiceService.createInvoice(invoice);
	}
	
	@RequestMapping(
			value = "/invoice/getAllInvoices",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public Collection<Invoice> getAllInvoices() {
		return invoiceService.getAllInvoices();
	}
	
	@RequestMapping(
			value = "/invoice/delete",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	public String deleteInvoice(@RequestBody Long id) {
		return invoiceService.deleteInvoice(id);
	}

	
	@RequestMapping(
			value = "/invoice/getReceivedInvoices",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public Collection<Invoice> getReceivedInvoices() {
		return invoiceService.getReceivedInvoices();
	}
	
	@RequestMapping(
			value = "/invoice/getSentInvoices",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public Collection<Invoice> getSentInvoices() {
		return invoiceService.getSentInvoices();
	}
	
	@RequestMapping(
			value = "/invoice/pay",
			method = RequestMethod.POST,
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	public String createPaymentOrder(@RequestBody PaymentOrderModel paymentOrderModel) {
		return SOAPClientService.sendPaymentOrder(paymentOrderModel);
	}
}

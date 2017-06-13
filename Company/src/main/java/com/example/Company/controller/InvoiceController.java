package com.example.Company.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Company.model.Invoice;
import com.example.Company.model.pojo.PaymentOrderModel;
import com.example.Company.service.InvoiceService;
import com.example.Company.service.SOAPClientService;
import com.example.Company.service.SaveToXml;

@RestController
public class InvoiceController {
	
	static {
	    SaveToXml.disableSslVerification();
	}

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
			value = "/invoice/getXML",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public List<String> getXML() {
		System.out.println("Usaooo");
		return invoiceService.getXML();
	}
	
	@RequestMapping(
			value = "/invoice/getBody",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_XML_VALUE
			)
	public void getBody(@RequestBody String params) {
		invoiceService.getBody(params);
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
			value = "/invoice/export",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	public String export(@RequestBody Long id) {
		return invoiceService.export(id);
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
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	public String createPaymentOrder(@RequestBody PaymentOrderModel paymentOrderModel) {
		return SOAPClientService.sendPaymentOrder(paymentOrderModel);
	}
	
	@RequestMapping(
			value = "/invoice/get/{invoiceId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public Invoice getInvoice(@PathVariable("invoiceId") Long invoiceId) {
		return invoiceService.getInvoice(invoiceId);
	}
}

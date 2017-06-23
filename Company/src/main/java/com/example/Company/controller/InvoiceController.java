package com.example.Company.controller;

import java.util.Collection;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Company.model.Invoice;
import com.example.Company.model.pojo.PaymentOrderPojo;
import com.example.Company.service.InvoiceService;
import com.example.Company.service.SOAPClientService;
import com.example.Company.service.SaveToXml;
import com.example.Company.service.XMLsecurity.EncryptedStringXmlAdapter;

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
			value = "/invoice/receiveXML",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_XML_VALUE
			)
	public Response getBody(@RequestBody String xmlParams) {
		System.out.println("Usaooo");
		invoiceService.receiveXML(xmlParams);
		return Response.status(201).entity("ok").build();
	}
	
	@RequestMapping(
			value = "/invoice/export/{invoiceId}",
			method = RequestMethod.GET,
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	public String export(@PathVariable("invoiceId") Long invoiceId) {
		return invoiceService.exportInvoiceToXML(invoiceId);
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
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	public String createPaymentOrder(@RequestBody PaymentOrderPojo paymentOrderModel) {
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
	
	@RequestMapping(value = "/invoice/receiveKey",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response receiveKey(@RequestBody String key) {
		EncryptedStringXmlAdapter.setKey(key);
		return Response.status(200).entity("ok").build();
	}
	
	@RequestMapping(
			value = "/invoice/receive",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_XML_VALUE
			)
	public Response receiveInvoice(@RequestBody com.example.service.invoice.Invoice invoice) {
		System.out.println("Usaooooooooo");
		invoiceService.receiveInvoice(invoice);
		return Response.status(201).entity("ok").build();
	}
	
	@RequestMapping(
			value = "/invoice/send/{invoiceId}",
			method = RequestMethod.GET,
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	public String sendInvoice(@PathVariable("invoiceId") Long invoiceId){
		 return invoiceService.sendInvoice(invoiceId);
	}
}

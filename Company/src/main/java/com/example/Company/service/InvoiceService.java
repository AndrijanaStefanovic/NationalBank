package com.example.Company.service;

import java.util.Collection;
import java.util.List;

import com.example.Company.model.Invoice;

public interface InvoiceService {

	public String createInvoice(Invoice invoice);

	public Collection<Invoice> getAllInvoices();
	
	public String deleteInvoice(Long id);
	
	public Collection<Invoice> getReceivedInvoices();
	
	public Collection<Invoice> getSentInvoices();
	
	public String exportInvoiceToXML(Long id);
	
	public Invoice getInvoice(Long id);
	
	public String receiveInvoice(com.example.service.invoice.Invoice invoice);
	
	public String sendInvoice(Long id);

	public String receiveXML(String xmlParams);

}

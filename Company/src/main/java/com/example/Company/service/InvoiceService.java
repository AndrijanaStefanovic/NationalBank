package com.example.Company.service;

import java.util.Collection;

import com.example.Company.model.Invoice;

public interface InvoiceService {

	public String createInvoice(Invoice invoice);

	public Collection<Invoice> getAllInvoices();
	
	public String deleteInvoice(Long id);
	
	public Collection<Invoice> getReceivedInvoices();
	
	public Collection<Invoice> getSentInvoices();
	
	public Invoice getInvoice(Long id);
	
}

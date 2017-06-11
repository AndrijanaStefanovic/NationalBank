package com.example.Company.service;

import java.util.Collection;

import com.example.Company.model.Invoice;
import com.example.Company.model.pojo.PaymentOrderModel;

public interface InvoiceService {

	public String createInvoice(Invoice invoice);

	public Collection<Invoice> getAllInvoices();
	
	public String deleteInvoice(Long id);
	
	public Collection<Invoice> getReceivedInvoices();
	
	public Collection<Invoice> getSentInvoices();

	public String export(Long id);
	
}

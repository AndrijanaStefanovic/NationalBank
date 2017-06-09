package com.example.Company.service;

import java.util.Collection;

import com.example.Company.model.InvoiceItem;

public interface InvoiceItemService {

	public String createInvoiceItem(Long invoiceId, InvoiceItem invoiceItem);
	
	public Collection<InvoiceItem> getInvoiceItems(Long invoiceId);
	
	public String deleteInvoiceItem(Long id);
}

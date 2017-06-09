package com.example.Company.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Company.model.Invoice;
import com.example.Company.repository.InvoiceRepository;

@Service
public class InvoiceServiceImpl implements InvoiceService{

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Override
	public String createInvoice(Invoice invoice) {
		invoiceRepository.save(invoice);
		return "200";
	}

	@Override
	public Collection<Invoice> getAllInvoices() {
		return invoiceRepository.findAll(null).getContent();
	}

	@Override
	public String deleteInvoice(Long id) {
		Invoice invoice = invoiceRepository.findOne(id);
		if(invoice.getInvoiceItems() != null && !invoice.getInvoiceItems().isEmpty()) {
			return "HasItems";
		}
		invoiceRepository.delete(invoice);
		return "200";
	}
	
	
}

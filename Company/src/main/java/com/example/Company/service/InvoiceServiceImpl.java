package com.example.Company.service;

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
	
	
}

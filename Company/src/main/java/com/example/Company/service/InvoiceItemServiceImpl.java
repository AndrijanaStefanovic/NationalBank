package com.example.Company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Company.model.InvoiceItem;
import com.example.Company.repository.InvoiceItemRepository;

@Service
public class InvoiceItemServiceImpl implements InvoiceItemService {

	@Autowired
	private InvoiceItemRepository invoiceItemRepository;
	
	@Override
	public String createInvoiceItem(InvoiceItem invoiceItem) {
		invoiceItemRepository.save(invoiceItem);
		return "200";
	}

}

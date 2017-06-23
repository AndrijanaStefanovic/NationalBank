package com.example.Company.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Company.model.Invoice;
import com.example.Company.model.InvoiceItem;
import com.example.Company.repository.InvoiceItemRepository;
import com.example.Company.repository.InvoiceRepository;

@Service
public class InvoiceItemServiceImpl implements InvoiceItemService {

	@Autowired
	private InvoiceItemRepository invoiceItemRepository;
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Override
	public String createInvoiceItem(Long invoiceId, InvoiceItem invoiceItem) {
		Invoice invoice = invoiceRepository.findOne(invoiceId);
		
		int number = 1;
		for(InvoiceItem ii : invoice.getInvoiceItems()){
			if(ii.getNumber() > number){
				number = ii.getNumber();
			}
		}
		invoiceItem.setNumber(++number);
		invoiceItem.setInvoice(invoice);
		invoiceItem.setValue(invoiceItem.getAmount() * invoiceItem.getUnitPrice());
		invoiceItem.setTotalDiscount(invoiceItem.getDiscountPercent() / 100.0 * invoiceItem.getValue());
		invoiceItem.setSubtractedDiscount(invoiceItem.getTotalDiscount());
		invoiceItem.setTotalTax(invoiceItem.getValue() * 20 / 100.0);
		invoiceItemRepository.save(invoiceItem);
		
		if(invoiceItem.getKind() != null && invoiceItem.getKind().equals("merchandise")){
			invoice.setMerchandiseValue(invoice.getMerchandiseValue() + invoiceItem.getValue());
		} else {
			invoice.setServicesValue(invoice.getServicesValue() + invoiceItem.getValue());
			invoiceItem.setMeasurmentUnit("");
		}
		invoice.setTotalValue(invoice.getTotalValue() + invoiceItem.getValue());
		invoice.setTotalDiscount(invoice.getTotalDiscount() + invoiceItem.getTotalDiscount());
		invoice.setTotalTax(invoice.getTotalTax() + invoiceItem.getTotalTax());
		invoice.setTotalDue(invoice.getTotalDue() + invoiceItem.getValue() + invoiceItem.getTotalTax() - invoiceItem.getTotalDiscount());
		invoiceRepository.save(invoice);
		return "200";
	}

	@Override
	public Collection<InvoiceItem> getInvoiceItems(Long invoiceId) {
		Invoice invoice = invoiceRepository.findOne(invoiceId);
		return invoiceItemRepository.findByInvoice(invoice);
	}

	@Override
	public String deleteInvoiceItem(Long id) {
		InvoiceItem invoiceItem = invoiceItemRepository.findOne(id);
		Invoice invoice = invoiceItem.getInvoice();
		
		if(invoiceItem.getKind() != null && invoiceItem.getKind().equals("merchandise")){
			invoice.setMerchandiseValue(invoice.getMerchandiseValue() - invoiceItem.getValue());
		} else {
			invoice.setServicesValue(invoice.getServicesValue() - invoiceItem.getValue());
		}
		invoice.setTotalValue(invoice.getTotalValue() - invoiceItem.getValue());
		invoice.setTotalDiscount(invoice.getTotalDiscount() - invoiceItem.getTotalDiscount());
		invoice.setTotalTax(invoice.getTotalTax() - invoiceItem.getTotalTax());
		invoice.setTotalDue(invoice.getTotalDue() - invoiceItem.getValue() - invoiceItem.getTotalTax() + invoiceItem.getTotalDiscount());
		invoiceRepository.save(invoice);
		
		invoiceItemRepository.delete(invoiceItem);
		
		return "200";
	}

}

package com.example.Company.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import com.example.Company.model.InvoiceItem;

public interface InvoiceItemRepository extends Repository<InvoiceItem, Long> {

	public InvoiceItem save(InvoiceItem entity);
	
	public InvoiceItem findOne(long id);
	
	public Page<InvoiceItem> findAll(Pageable pageable);
}

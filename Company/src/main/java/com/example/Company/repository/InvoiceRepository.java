package com.example.Company.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import com.example.Company.model.Invoice;

public interface InvoiceRepository extends Repository<Invoice, Long>{

	public Invoice save(Invoice entity);
	
	public Invoice findOne(long id);
	
	public Page<Invoice> findAll(Pageable pageable);
	
	public void delete(Invoice entity);
	
	public List<Invoice> findByReceived(boolean received);
}

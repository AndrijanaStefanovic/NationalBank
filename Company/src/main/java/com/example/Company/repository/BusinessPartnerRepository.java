package com.example.Company.repository;

import java.awt.print.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.Repository;
import com.example.Company.model.BusinessPartner;

public interface BusinessPartnerRepository extends Repository<BusinessPartner, Long> {

	public BusinessPartner findById(Long id);
	
	public BusinessPartner add(BusinessPartner businessPartner);
	
	public void delete(Long id);
	
	public Page<BusinessPartner> findAll(Pageable pageable);
}

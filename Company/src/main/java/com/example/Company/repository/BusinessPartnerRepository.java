package com.example.Company.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import com.example.Company.model.BusinessPartner;

public interface BusinessPartnerRepository extends Repository<BusinessPartner, Long> {

	public BusinessPartner findOne(Long id);
	
	public BusinessPartner save(BusinessPartner entity);
	
	public void delete(BusinessPartner entity);
	
	public Page<BusinessPartner> findAll(Pageable pageable);
}

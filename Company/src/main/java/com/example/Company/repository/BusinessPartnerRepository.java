package com.example.Company.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import com.example.Company.model.BusinessPartner;
import com.example.Company.model.Company;

public interface BusinessPartnerRepository extends Repository<BusinessPartner, Long> {

	public BusinessPartner findOne(Long id);
	
	public List<BusinessPartner> findByCompany(Company company);
	
	public BusinessPartner save(BusinessPartner entity);
	
	public void delete(BusinessPartner entity);
	
	public Page<BusinessPartner> findAll(Pageable pageable);
	
	public List<BusinessPartner> findByPartnerPIB(String partnerPIB);
}

package com.example.Company.service;

import java.util.Collection;
import com.example.Company.model.BusinessPartner;

public interface BusinessPartnerService {
	
	public String create(Long companyId, BusinessPartner entity);
	
	public BusinessPartner findOne(Long id);	
	
	public Collection<BusinessPartner> getAllBusinessPartners(Long companyId);
	
	public String delete(Long id);
}

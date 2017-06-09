package com.example.Company.service;

import java.util.Collection;
import com.example.Company.model.BusinessPartner;

public interface BusinessPartnerService {
	
	public BusinessPartner create(BusinessPartner entity);
	
	public BusinessPartner findOne(Long id);	
	
	public Collection<BusinessPartner> getAllBusinessPartners();
	
	public String delete(Long id);
}

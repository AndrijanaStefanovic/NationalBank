package com.example.Company.service;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Company.model.BusinessPartner;
import com.example.Company.repository.BusinessPartnerRepository;

@Service
public class BusinessPartnerServiceIml implements BusinessPartnerService {

	@Autowired
	private BusinessPartnerRepository businessPartnerRepostory;
	
	@Override
	public String create(BusinessPartner businessPartner) {
		businessPartnerRepostory.save(businessPartner);
		return "200";
	}

	@Override
	public BusinessPartner findOne(Long id) {
		return businessPartnerRepostory.findOne(id);
	}

	@Override
	public String delete(Long id) {
		if (id != null) {
			BusinessPartner businessPartner = businessPartnerRepostory.findOne(id);
			businessPartnerRepostory.delete(businessPartner);
			return "200";
		} else 
			return "500";		
	}

	@Override
	public Collection<BusinessPartner> getAllBusinessPartners() {
		return businessPartnerRepostory.findAll(null).getContent();
	}
}

package com.example.Company.service;

import java.awt.print.Pageable;

import org.apache.cxf.common.i18n.Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.Company.model.BusinessPartner;
import com.example.Company.repository.BusinessPartnerRepository;

@Service
public class BusinessPartnerServiceIml implements BusinessPartnerService {

	@Autowired
	private BusinessPartnerRepository businessPartnerRepostory;
	
	@Override
	public BusinessPartner add(BusinessPartner businessPartner) {
		return businessPartnerRepostory.add(businessPartner);
	}

	@Override
	public BusinessPartner findById(Long id) {
		return businessPartnerRepostory.findById(id);
	}

	@Override
	public void delete(Long id) {
		BusinessPartner businessPartner = businessPartnerRepostory.findById(id);
		
		if (businessPartner != null) {
			businessPartnerRepostory.delete(id);
		} else {
			try {
				throw new java.lang.Exception("Business Partner can not be find!");
			} catch (Exception e) {
				e.printStackTrace();
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}		
	}

	@Override
	public Page<BusinessPartner> findAll(Pageable pageable) {
		return businessPartnerRepostory.findAll(pageable);
	}

}

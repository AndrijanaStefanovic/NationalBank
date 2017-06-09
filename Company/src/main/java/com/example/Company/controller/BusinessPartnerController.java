package com.example.Company.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Company.model.BusinessPartner;
import com.example.Company.service.BusinessPartnerService;

@RestController
public class BusinessPartnerController {
	
	@Autowired
	private BusinessPartnerService partnerService;
	
	@RequestMapping(
			value = "/businesspartner/add",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	
	public BusinessPartner add(@RequestBody BusinessPartner partner) {
		return partnerService.add(partner);
	}
	
	@RequestMapping(
			value = "/businesspartner/getAllBusinessPartners",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	
	public Collection<BusinessPartner> getAllBusinessPartners() {
		return partnerService.findAll(null).getContent();
	}

}

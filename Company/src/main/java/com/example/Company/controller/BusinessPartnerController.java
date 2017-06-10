package com.example.Company.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
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
			value = "/businesspartner/create/{companyId}",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
			)	
	public String create(@PathVariable("companyId") Long companyId, @RequestBody BusinessPartner partner) {
		return partnerService.create(companyId, partner);
	}
	
	@RequestMapping(
			value = "/businesspartner/getAllBusinessPartners/{companyId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
			)	
	public Collection<BusinessPartner> getAllBusinessPartners(@PathVariable("companyId") Long companyId) {
		return partnerService.getAllBusinessPartners(companyId);
	}
	
	@RequestMapping(
			value = "/businesspartner/delete",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
			)	
	public String delete(@RequestBody Long id) {
		return partnerService.delete(id);
	}
}

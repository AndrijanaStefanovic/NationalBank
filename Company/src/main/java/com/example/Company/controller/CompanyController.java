package com.example.Company.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Company.model.Company;
import com.example.Company.service.CompanyService;

@RestController
public class CompanyController {

	@Autowired
	private CompanyService companyService;
	
	@RequestMapping(
			value = "/company/create",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
			)	
	public Company create(@RequestBody Company company) {
		return companyService.create(company);
	}
	
	@RequestMapping(
			value = "/company/getAllCompanies",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
			)	
	public Collection<Company> getAllCompanies() {
		return companyService.getAllCompanies();
	}
	
	@RequestMapping(
			value = "/company/delete",
			method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	public String delete(@RequestBody Long id) {
		return companyService.delete(id);
	}
	
	
}

package com.example.Company.service;

import java.util.Collection;

import com.example.Company.model.Company;

public interface CompanyService {
	
	public Company create(Company entity);
	
	public Company findOne(Long id);
	
	public String delete(Long id);
	
	public Collection<Company> getAllCompanies();

}

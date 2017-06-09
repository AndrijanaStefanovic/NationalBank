package com.example.Company.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.Company.model.Company;

public interface CompanyService {
	
	public Company findById(Long id);
	
	public Company add(Company company);
	
	public void delete(Long id);
	
	public Page<Company> findAll(Pageable pageable);

}

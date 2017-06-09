package com.example.Company.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import com.example.Company.model.Company;

public interface CompanyRepository extends Repository<Company, Long> {

	public Company findById(Long id);
	
	public Company save(Company company);
	
	public void delete(Long id);
	
	public Page<Company> findAll(Pageable pageable);

}

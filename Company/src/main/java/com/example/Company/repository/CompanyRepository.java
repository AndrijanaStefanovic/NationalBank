package com.example.Company.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import com.example.Company.model.Company;

public interface CompanyRepository extends Repository<Company, Long> {

	public Company findOne(Long id);
	
	public Company save(Company entity);
	
	public void delete(Company entity);
	
	public Page<Company> findAll(Pageable pageable);

	public List<Company> findByCompanyPIB(String companyPIB);
	
	public List<Company> findByCompanyAccount(String companyAccount);
}

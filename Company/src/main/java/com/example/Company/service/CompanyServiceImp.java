package com.example.Company.service;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Company.model.Company;
import com.example.Company.repository.CompanyRepository;

@Service
public class CompanyServiceImp implements CompanyService {

	@Autowired
	private CompanyRepository companyRepository;
	
	@Override
	public String create(Company company) {
		companyRepository.save(company);
		return "200";
	}
	
	@Override
	public Company findOne(Long id) {
		return companyRepository.findOne(id);
	}

	@Override
	public String delete(Long id) {
		if (id != null) {
			Company company = companyRepository.findOne(id);
			companyRepository.delete(company);
			return "200";
		} else 
			return "500";		
	}

	@Override
	public Collection<Company> getAllCompanies() {
		return companyRepository.findAll(null).getContent();
	}
}


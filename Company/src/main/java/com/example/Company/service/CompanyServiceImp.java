package com.example.Company.service;

import java.awt.print.Pageable;

import org.apache.cxf.common.i18n.Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.example.Company.model.Company;
import com.example.Company.repository.CompanyRepository;

@Service
public class CompanyServiceImp implements CompanyService {

	@Autowired
	private CompanyRepository companyRepository;
	
	@Override
	public Company add(Company company) {
		return companyRepository.add(company);
	}

	@Override
	public Company findById(Long id) {
		return companyRepository.findById(id);
	}

	@Override
	public void delete(Long id) {
		Company company = companyRepository.findById(id);
		
		if (company != null) {
			companyRepository.delete(id);
		} else {
			try {
				throw new java.lang.Exception("Company can not be find!");
			} catch (Exception e) {
				e.printStackTrace();
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}		
	}

	@Override
	public Page<Company> findAll(Pageable pageable) {
		return companyRepository.findAll(pageable);
	}

}


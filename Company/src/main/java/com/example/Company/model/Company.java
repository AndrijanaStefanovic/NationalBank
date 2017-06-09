package com.example.Company.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Company {
	
	public Company() {}

	@Id
	@GeneratedValue
	private Long id;	

	@Column(length = 255, nullable = false)
	private String name;
	
	@Column(length = 11, nullable = false)
	private String companyPIB;

	@Column(length = 55)
	private String companyAddress;

	@Column(length = 8, nullable = false)
	private String companyMBR; 
	
	@Column(length = 30)
	private String mobile;
	
	@OneToMany(mappedBy = "company")
	private List<BusinessPartner> businessPartners;

	public Company(String name, 
				   String companyPIB, 
				   String companyAddress, 
				   String companyMBR, 
				   String mobile,
				   List<BusinessPartner> businessPartners) {
		super();
		this.name = name;
		this.companyPIB = companyPIB;
		this.companyAddress = companyAddress;
		this.companyMBR = companyMBR;
		this.mobile = mobile;
		this.businessPartners = businessPartners;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyPIB() {
		return companyPIB;
	}

	public void setCompanyPIB(String companyPIB) {
		this.companyPIB = companyPIB;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyMBR() {
		return companyMBR;
	}

	public void setCompanyMBR(String companyMBR) {
		this.companyMBR = companyMBR;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<BusinessPartner> getBusinessPartners() {
		return businessPartners;
	}

	public void setBusinessPartners(List<BusinessPartner> businessPartners) {
		this.businessPartners = businessPartners;
	}
	
}

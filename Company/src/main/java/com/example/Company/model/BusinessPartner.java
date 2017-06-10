package com.example.Company.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class BusinessPartner {

	public BusinessPartner() {}
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length = 255, nullable = false)
	private String name;
	
	@Column(length = 20, nullable = false)
	private String kind;
	
	@Column(length = 55)
	private String partnerAddress;
	
	@Column(length = 11, nullable = false)
	private String partnerPIB;
	
	@Column(length = 18, nullable = false)
	private String partnerAccount;	
	
	@Column(length = 30)
	private String email;	
	
	@ManyToOne
	private Company company;

	public BusinessPartner(String name, 
						   String kind, 
						   String partnerAddress, 
						   String partnerPIB,
						   String partnerAccount, 
						   String email, 
						   Company company) {
		super();
		this.name = name;
		this.kind = kind;
		this.partnerAddress = partnerAddress;
		this.partnerPIB = partnerPIB;
		this.partnerAccount = partnerAccount;
		this.email = email;
		this.company = company;
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

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getPartnerAddress() {
		return partnerAddress;
	}

	public void setPartnerAddress(String partnerAddress) {
		this.partnerAddress = partnerAddress;
	}

	public String getPartnerPIB() {
		return partnerPIB;
	}

	public void setPartnerPIB(String partnerPIB) {
		this.partnerPIB = partnerPIB;
	}

	public String getPartnerAccount() {
		return partnerAccount;
	}

	public void setPartnerAccount(String partnerAccount) {
		this.partnerAccount = partnerAccount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}

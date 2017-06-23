package com.example.Company.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class BusinessPartner {

	public BusinessPartner() {}
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length = 255, nullable = false)
	private String name;
	
	@Column(length = 20, nullable = false)
	private String kind; // customer or supplier
	
	@Column(length = 11, nullable = false)
	private String partnerPIB;
	
	@Column(length = 20, nullable = false)
	private String partnerAccount;	

	@Column(length = 20, nullable = false)
	private String referenceNumber;
	
	@Column(length = 2, nullable = false)
	private String model;
	
	@Column(length = 55)
	private String partnerAddress;
	
	@Column(length = 30)
	private String email;	
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id", nullable = false)
	private Company company;

	@Column
	private String url;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public BusinessPartner(String name, 
						   String kind, 
						   String partnerPIB, 
						   String partnerAccount, 
						   String referenceNumber,
						   String model, 
						   String partnerAddress, 
						   String email, 
						   Company company) {
		super();
		this.name = name;
		this.kind = kind;
		this.partnerPIB = partnerPIB;
		this.partnerAccount = partnerAccount;
		this.referenceNumber = referenceNumber;
		this.model = model;
		this.partnerAddress = partnerAddress;
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

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getPartnerAddress() {
		return partnerAddress;
	}

	public void setPartnerAddress(String partnerAddress) {
		this.partnerAddress = partnerAddress;
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

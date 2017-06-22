package com.example.Bank.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Bank {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private int code; //3 cifre
	
	@Column
	private String swiftCode;
	
	@Column
	private String accountNumber;

	@OneToMany(mappedBy = "bank")
	private List<Account> accounts;
	
	public Bank(){}
	
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

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getSWIFTcode() {
		return swiftCode;
	}

	public void setSWIFTcode(String sWIFTcode) {
		swiftCode = sWIFTcode;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	
	
}

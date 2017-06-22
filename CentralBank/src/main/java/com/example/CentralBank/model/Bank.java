package com.example.CentralBank.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
	
	@Column
	private double accountBalance;
	
	@Column
	private String url; //oblik: https://localhost:port/ws/
	
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

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String sWIFTcode) {
		swiftCode = sWIFTcode;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUri(String url) {
		this.url = url;
	}

	public void calculateAccountBalance(double sum) {
		this.accountBalance += sum;
	}
}

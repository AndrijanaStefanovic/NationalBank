package com.example.Bank.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class AccountModel {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String accountNumber;

	@OneToMany(fetch = FetchType.LAZY, mappedBy="account")
	private List<DailyAccountBalance> dailyAccountBalances;
	
	public AccountModel(){}
	
	public AccountModel(String accountNumber) {
		super();
		this.accountNumber = accountNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public List<DailyAccountBalance> getDailyAccountBalances() {
		return dailyAccountBalances;
	}

	public void setDailyAccountBalances(List<DailyAccountBalance> dailyAccountBalances) {
		this.dailyAccountBalances = dailyAccountBalances;
	}
	
	
	
}

package com.example.Bank.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Account {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String accountNumber;
	
	@Column
	private Double balance;

	@OneToMany(fetch = FetchType.EAGER, mappedBy="account")
	private List<DailyAccountBalance> dailyAccountBalances;
	
	@ManyToOne
	private Bank bank;
	
	public Account(){}
	
	public Account(String accountNumber) {
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

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}
	
	
	
}

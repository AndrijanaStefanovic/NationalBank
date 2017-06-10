package com.example.Bank.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class DailyAccountBalance {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private Date date;
	
	@Column
	private double previousBalance;
	
	@Column
	private int numberOfWithdrawals;
	
	@Column
	private double newBalance;
	
	@Column
	private int numberOfDeposits;

	@ManyToOne
	private AccountModel account;
	
	public DailyAccountBalance(){}
	
	public DailyAccountBalance(Date date, double previousBalance, int numberOfWithdrawals, double newBalance,
			int numberOfDeposits, AccountModel account) {
		super();
		this.date = date;
		this.previousBalance = previousBalance;
		this.numberOfWithdrawals = numberOfWithdrawals;
		this.newBalance = newBalance;
		this.numberOfDeposits = numberOfDeposits;
		this.account = account;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getPreviousBalance() {
		return previousBalance;
	}

	public void setPreviousBalance(double previousBalance) {
		this.previousBalance = previousBalance;
	}

	public int getNumberOfWithdrawals() {
		return numberOfWithdrawals;
	}

	public void setNumberOfWithdrawals(int numberOfWithdrawals) {
		this.numberOfWithdrawals = numberOfWithdrawals;
	}

	public double getNewBalance() {
		return newBalance;
	}

	public void setNewBalance(double newBalance) {
		this.newBalance = newBalance;
	}

	public int getNumberOfDeposits() {
		return numberOfDeposits;
	}

	public void setNumberOfDeposits(int numberOfDeposits) {
		this.numberOfDeposits = numberOfDeposits;
	}



	public AccountModel getAccount() {
		return account;
	}



	public void setAccount(AccountModel account) {
		this.account = account;
	}
	
	
	
	
}

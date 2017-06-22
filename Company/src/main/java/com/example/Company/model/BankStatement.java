package com.example.Company.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class BankStatement {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String accountNumber;

	@Column
	private int sectionNumber;

	@Column
	private Date date;

	@Column
	private double previousBalance;

	@Column
	private int numberOfDeposits;

	@Column
	private double totalDeposited;

	@Column
	private int numberOfWithdrawals;

	@Column
	private double totalWithdrawn;

	@Column
	private double newBalance;
	
	@JsonIgnore
	@OneToMany(mappedBy = "bankStatement")
	private List<BankStatementItem> bankStatementItems;

	public BankStatement() {

	}

	public BankStatement(String accountNumber, int sectionNumber, Date date, double previousBalance,
			int numberOfDeposits, double totalDeposited, int numberOfWithdrawals, double totalWithdrawn,
			double newBalance) {
		super();
		this.accountNumber = accountNumber;
		this.sectionNumber = sectionNumber;
		this.date = date;
		this.previousBalance = previousBalance;
		this.numberOfDeposits = numberOfDeposits;
		this.totalDeposited = totalDeposited;
		this.numberOfWithdrawals = numberOfWithdrawals;
		this.totalWithdrawn = totalWithdrawn;
		this.newBalance = newBalance;
		this.bankStatementItems = bankStatementItems;
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

	public int getSectionNumber() {
		return sectionNumber;
	}

	public void setSectionNumber(int sectionNumber) {
		this.sectionNumber = sectionNumber;
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

	public int getNumberOfDeposits() {
		return numberOfDeposits;
	}

	public void setNumberOfDeposits(int numberOfDeposits) {
		this.numberOfDeposits = numberOfDeposits;
	}

	public double getTotalDeposited() {
		return totalDeposited;
	}

	public void setTotalDeposited(double totalDeposited) {
		this.totalDeposited = totalDeposited;
	}

	public int getNumberOfWithdrawals() {
		return numberOfWithdrawals;
	}

	public void setNumberOfWithdrawals(int numberOfWithdrawals) {
		this.numberOfWithdrawals = numberOfWithdrawals;
	}

	public double getTotalWithdrawn() {
		return totalWithdrawn;
	}

	public void setTotalWithdrawn(double totalWithdrawn) {
		this.totalWithdrawn = totalWithdrawn;
	}

	public double getNewBalance() {
		return newBalance;
	}

	public void setNewBalance(double newBalance) {
		this.newBalance = newBalance;
	}

	public List<BankStatementItem> getBankStatementItems() {
		return bankStatementItems;
	}

	public void setBankStatementItems(List<BankStatementItem> bankStatementItems) {
		this.bankStatementItems = bankStatementItems;
	}

}

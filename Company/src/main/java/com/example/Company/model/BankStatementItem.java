package com.example.Company.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class BankStatementItem {

	@Id
	@GeneratedValue
	private long id;
	
	@Column
	private String debtor;
	
	@Column
	private String paymentPurpose;
	
	@Column
	private String creditor;
	
	@Column
	private Date dateOfAccount;
	
	@Column
	private Date dateOfValue;
	
	@Column
	private String debtorAccount;
	
	@Column
	private int debtorModel;
	
	@Column
	private String debtorReferenceNumber;
	
	@Column
	private String creditorAccount;
	
	@Column
	private int creditorModel;
	
	@Column
	private String creditorReferenceNumber;
	
	@Column
	private double amount;
	
	@Column 
	private String direction;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private BankStatement bankStatement;
	
	public BankStatementItem() {
		
	}

	public BankStatementItem(String debtor, String paymentPurpose, String creditor, Date dateOfAccount,
			Date dateOfValue, String debtorAccount, int debtorModel, String debtorReferenceNumber,
			String creditorAccount, int creditorModel, String creditorReferenceNumber, double amount,
			String direction, BankStatement bankStatement) {
		super();
		this.debtor = debtor;
		this.paymentPurpose = paymentPurpose;
		this.creditor = creditor;
		this.dateOfAccount = dateOfAccount;
		this.dateOfValue = dateOfValue;
		this.debtorAccount = debtorAccount;
		this.debtorModel = debtorModel;
		this.debtorReferenceNumber = debtorReferenceNumber;
		this.creditorAccount = creditorAccount;
		this.creditorModel = creditorModel;
		this.creditorReferenceNumber = creditorReferenceNumber;
		this.amount = amount;
		this.direction = direction;
		this.bankStatement = bankStatement;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDebtor() {
		return debtor;
	}

	public void setDebtor(String debtor) {
		this.debtor = debtor;
	}

	public String getPaymentPurpose() {
		return paymentPurpose;
	}

	public void setPaymentPurpose(String paymentPurpose) {
		this.paymentPurpose = paymentPurpose;
	}

	public String getCreditor() {
		return creditor;
	}

	public void setCreditor(String creditor) {
		this.creditor = creditor;
	}

	public Date getDateOfAccount() {
		return dateOfAccount;
	}

	public void setDateOfAccount(Date dateOfAccount) {
		this.dateOfAccount = dateOfAccount;
	}

	public Date getDateOfValue() {
		return dateOfValue;
	}

	public void setDateOfValue(Date dateOfValue) {
		this.dateOfValue = dateOfValue;
	}

	public String getDebtorAccount() {
		return debtorAccount;
	}

	public void setDebtorAccount(String debtorAccount) {
		this.debtorAccount = debtorAccount;
	}

	public int getDebtorModel() {
		return debtorModel;
	}

	public void setDebtorModel(int debtorModel) {
		this.debtorModel = debtorModel;
	}

	public String getDebtorReferenceNumber() {
		return debtorReferenceNumber;
	}

	public void setDebtorReferenceNumber(String debtorReferenceNumber) {
		this.debtorReferenceNumber = debtorReferenceNumber;
	}

	public String getCreditorAccount() {
		return creditorAccount;
	}

	public void setCreditorAccount(String creditorAccount) {
		this.creditorAccount = creditorAccount;
	}

	public int getCreditorModel() {
		return creditorModel;
	}

	public void setCreditorModel(int creditorModel) {
		this.creditorModel = creditorModel;
	}

	public String getCreditorReferenceNumber() {
		return creditorReferenceNumber;
	}

	public void setCreditorReferenceNumber(String creditorReferenceNumber) {
		this.creditorReferenceNumber = creditorReferenceNumber;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String isDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public BankStatement getBankStatement() {
		return bankStatement;
	}

	public void setBankStatement(BankStatement bankStatement) {
		this.bankStatement = bankStatement;
	}
	
}

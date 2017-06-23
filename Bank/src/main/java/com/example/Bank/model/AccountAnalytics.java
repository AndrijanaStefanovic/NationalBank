package com.example.Bank.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AccountAnalytics {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private boolean received;
	
	@Column
	private String debtorsBankSWIFT;
	
	@Column
	private String debtorsBankAccount;
	
	@Column
	private String creditorsBankSWIFT;
	
	@Column
	private String creditorsBankAccount;
	
	@Column
	private String debtor;
	
	@Column
	private String paymentPurpose;
	
	@Column 
	private String creditor;
	
	@Column
	private Date dateOfPaymentOrder;
	
	@Column
	private Date dateOfValue;
	
	@Column
	private String debtorsAccountNumber;
	
	@Column
	private int debtorsModel;
	
	@Column
	private String debtorsReferenceNumber;
	
	@Column
	private String creditorsAccountNumber;
	
	@Column
	private int creditorsModel;
	
	@Column
	private String creditorsReferenceNumber;
	
	@Column
	private double amount;
	
	@Column
	private double reservedFunds;

	@Column 
	private String currency;
	
	@ManyToOne
	private DailyAccountBalance dailyAccountBalance;
	
	public AccountAnalytics(){}

	public AccountAnalytics(String debtorsBankSWIFT, String debtorsBankAccount, String creditorsBankSWIFT,
			String creditorsBankAccount, String debtor, String paymentPurpose, String creditor, Date dateOfPaymentOrder,
			Date dateOfValue, String debtorsAccountNumber, int debtorsModel, String debtorsReferenceNumber,
			String creditorsAccountNumber, int creditorsModel, String creditorsReferenceNumber, double amount,
			double reservedFunds, String currency, boolean received) {
		super();
		this.debtorsBankSWIFT = debtorsBankSWIFT;
		this.debtorsBankAccount = debtorsBankAccount;
		this.creditorsBankSWIFT = creditorsBankSWIFT;
		this.creditorsBankAccount = creditorsBankAccount;
		this.debtor = debtor;
		this.paymentPurpose = paymentPurpose;
		this.creditor = creditor;
		this.dateOfPaymentOrder = dateOfPaymentOrder;
		this.dateOfValue = dateOfValue;
		this.debtorsAccountNumber = debtorsAccountNumber;
		this.debtorsModel = debtorsModel;
		this.debtorsReferenceNumber = debtorsReferenceNumber;
		this.creditorsAccountNumber = creditorsAccountNumber;
		this.creditorsModel = creditorsModel;
		this.creditorsReferenceNumber = creditorsReferenceNumber;
		this.amount = amount;
		this.reservedFunds = reservedFunds;
		this.currency = currency;
		this.received = received;
	}

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getDebtorsBankSWIFT() {
		return debtorsBankSWIFT;
	}



	public void setDebtorsBankSWIFT(String debtorsBankSWIFT) {
		this.debtorsBankSWIFT = debtorsBankSWIFT;
	}



	public String getDebtorsBankAccount() {
		return debtorsBankAccount;
	}



	public void setDebtorsBankAccount(String debtorsBankAccount) {
		this.debtorsBankAccount = debtorsBankAccount;
	}



	public String getCreditorsBankSWIFT() {
		return creditorsBankSWIFT;
	}



	public void setCreditorsBankSWIFT(String creditorsBankSWIFT) {
		this.creditorsBankSWIFT = creditorsBankSWIFT;
	}



	public String getCreditorsBankAccount() {
		return creditorsBankAccount;
	}



	public void setCreditorsBankAccount(String creditorsBankAccount) {
		this.creditorsBankAccount = creditorsBankAccount;
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



	public Date getDateOfPaymentOrder() {
		return dateOfPaymentOrder;
	}



	public void setDateOfPaymentOrder(Date dateOfPaymentOrder) {
		this.dateOfPaymentOrder = dateOfPaymentOrder;
	}



	public Date getDateOfValue() {
		return dateOfValue;
	}



	public void setDateOfValue(Date dateOfValue) {
		this.dateOfValue = dateOfValue;
	}



	public String getDebtorsAccountNumber() {
		return debtorsAccountNumber;
	}



	public void setDebtorsAccountNumber(String debtorsAccountNumber) {
		this.debtorsAccountNumber = debtorsAccountNumber;
	}



	public int getDebtorsModel() {
		return debtorsModel;
	}



	public void setDebtorsModel(int debtorsModel) {
		this.debtorsModel = debtorsModel;
	}



	public String getDebtorsReferenceNumber() {
		return debtorsReferenceNumber;
	}



	public void setDebtorsReferenceNumber(String debtorsReferenceNumber) {
		this.debtorsReferenceNumber = debtorsReferenceNumber;
	}



	public String getCreditorsAccountNumber() {
		return creditorsAccountNumber;
	}



	public void setCreditorsAccountNumber(String creditorsAccountNumber) {
		this.creditorsAccountNumber = creditorsAccountNumber;
	}



	public int getCreditorsModel() {
		return creditorsModel;
	}



	public void setCreditorsModel(int creditorsModel) {
		this.creditorsModel = creditorsModel;
	}



	public String getCreditorsReferenceNumber() {
		return creditorsReferenceNumber;
	}



	public void setCreditorsReferenceNumber(String creditorsReferenceNumber) {
		this.creditorsReferenceNumber = creditorsReferenceNumber;
	}



	public double getAmount() {
		return amount;
	}



	public void setAmount(double amount) {
		this.amount = amount;
	}



	public String getCurrency() {
		return currency;
	}



	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public boolean isReceived() {
		return received;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}

	public DailyAccountBalance getDailyAccountBalance() {
		return dailyAccountBalance;
	}

	public void setDailyAccountBalance(DailyAccountBalance dailyAccountBalance) {
		this.dailyAccountBalance = dailyAccountBalance;
	}
	
	public double getReservedFunds() {
		return reservedFunds;
	}

	public void setReservedFunds(double reservedFunds) {
		this.reservedFunds = reservedFunds;
	}

	
}

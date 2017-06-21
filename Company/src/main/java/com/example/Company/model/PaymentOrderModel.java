package com.example.Company.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PaymentOrderModel {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String debtorAccountNumber;
	
	@Column
	private String debtorReferenceNumber;
	
	@Column
	private String debtorInfo;
	
	@Column
	private int debtorModel;
	
	@Column
	private String creditorAccountNumber;
	
	@Column
	private String creditorReferenceNumber;
	
	@Column
	private String creditorInfo;
	
	@Column
	private int creditorModel;
	
	@Column
	private Date dateOfValue;
	
	@Column
	private Date dateOfPayment;
	
	@Column
	private String paymentPurpose;
	
	@Column
	private double amount;
	
	@Column
	private String currency;
	
	@Column
	private boolean urgent;
	
	public PaymentOrderModel(){}
	
	public PaymentOrderModel(String debtorAccountNumber, String debtorReferenceNumber, String debtorInfo,
			int debtorModel, String creditorAccountNumber, String creditorReferenceNumber, String creditorInfo,
			int creditorModel, Date dateOfValue, Date dateOfPayment, String paymentPurpose, double amount,
			String currency, boolean urgent) {
		super();
		this.debtorAccountNumber = debtorAccountNumber;
		this.debtorReferenceNumber = debtorReferenceNumber;
		this.debtorInfo = debtorInfo;
		this.debtorModel = debtorModel;
		this.creditorAccountNumber = creditorAccountNumber;
		this.creditorReferenceNumber = creditorReferenceNumber;
		this.creditorInfo = creditorInfo;
		this.creditorModel = creditorModel;
		this.dateOfValue = dateOfValue;
		this.dateOfPayment = dateOfPayment;
		this.paymentPurpose = paymentPurpose;
		this.amount = amount;
		this.currency = currency;
		this.urgent = urgent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDebtorAccountNumber() {
		return debtorAccountNumber;
	}

	public void setDebtorAccountNumber(String debtorAccountNumber) {
		this.debtorAccountNumber = debtorAccountNumber;
	}

	public String getDebtorReferenceNumber() {
		return debtorReferenceNumber;
	}

	public void setDebtorReferenceNumber(String debtorReferenceNumber) {
		this.debtorReferenceNumber = debtorReferenceNumber;
	}

	public String getDebtorInfo() {
		return debtorInfo;
	}

	public void setDebtorInfo(String debtorInfo) {
		this.debtorInfo = debtorInfo;
	}

	public int getDebtorModel() {
		return debtorModel;
	}

	public void setDebtorModel(int debtorModel) {
		this.debtorModel = debtorModel;
	}

	public String getCreditorAccountNumber() {
		return creditorAccountNumber;
	}

	public void setCreditorAccountNumber(String creditorAccountNumber) {
		this.creditorAccountNumber = creditorAccountNumber;
	}

	public String getCreditorReferenceNumber() {
		return creditorReferenceNumber;
	}

	public void setCreditorReferenceNumber(String creditorReferenceNumber) {
		this.creditorReferenceNumber = creditorReferenceNumber;
	}

	public String getCreditorInfo() {
		return creditorInfo;
	}

	public void setCreditorInfo(String creditorInfo) {
		this.creditorInfo = creditorInfo;
	}

	public int getCreditorModel() {
		return creditorModel;
	}

	public void setCreditorModel(int creditorModel) {
		this.creditorModel = creditorModel;
	}

	public Date getDateOfValue() {
		return dateOfValue;
	}

	public void setDateOfValue(Date dateOfValue) {
		this.dateOfValue = dateOfValue;
	}

	public Date getDateOfPayment() {
		return dateOfPayment;
	}

	public void setDateOfPayment(Date dateOfPayment) {
		this.dateOfPayment = dateOfPayment;
	}

	public String getPaymentPurpose() {
		return paymentPurpose;
	}

	public void setPaymentPurpose(String paymentPurpose) {
		this.paymentPurpose = paymentPurpose;
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

	public boolean isUrgent() {
		return urgent;
	}

	public void setUrgent(boolean urgent) {
		this.urgent = urgent;
	}
	
	
	
	
	
}

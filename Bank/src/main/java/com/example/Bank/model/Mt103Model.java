package com.example.Bank.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.example.service.mt103.Mt103;

@Entity
public class Mt103Model {
	
	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String messageId;

	@Column
	private String creditorBankSwift;

	@Column
	private String creditorBankAccountNumber;

	@Column
	private String debtorBankSwift;

	@Column
	private String debtorBankAccountNumber;

	@Column
	private double total;

	@Column
	private String currency;

	@Column
	private Date dateOfValue;

	@Column
	private Date dateOfPayment;
	
	@Column
	private String paymentPurpose;

	@Column
	private String creditorInfo;
	
	@Column
	private String creditorAccountNumber;
	
	@Column
	private String creditorReferenceNumber;

	@Column
	private int creditorModel;

	@Column
	private String debtorInfo;
	
	@Column
	private String debtorAccountNumber;
	
	@Column
	private String debtorReferenceNumber;

	@Column
	private int debtorModel;
	
	public Mt103Model() {
		
	}

	public Mt103Model(Mt103 mt103) {
		this.messageId = mt103.getMessageId();
		this.creditorBankSwift = mt103.getCreditorsBank().getSWIFT();
		this.creditorBankAccountNumber = mt103.getCreditorsBank().getAccountNumber();
		this.debtorBankSwift = mt103.getDebtorsBank().getSWIFT();
		this.debtorBankAccountNumber = mt103.getDebtorsBank().getAccountNumber();
		this.total = mt103.getTotal().doubleValue();
		this.currency = mt103.getCurrency();
		this.dateOfValue = mt103.getDateOfValue().toGregorianCalendar().getTime();
		this.dateOfPayment = mt103.getDateOfPayment().toGregorianCalendar().getTime();
		this.paymentPurpose = mt103.getPaymentPurpose();
		this.creditorInfo = mt103.getCreditor().getInfo();
		this.creditorAccountNumber = mt103.getCreditor().getAccountNumber();
		this.creditorReferenceNumber = mt103.getCreditor().getReferenceNumber();
		this.creditorModel = mt103.getCreditor().getModel();
		this.debtorInfo = mt103.getDebtor().getInfo();
		this.debtorAccountNumber = mt103.getDebtor().getAccountNumber();
		this.debtorReferenceNumber = mt103.getDebtor().getReferenceNumber();
		this.debtorModel = mt103.getDebtor().getModel();
		
	}
	
	public Mt103Model(String messageId, String creditorBankSwift, String creditorBankAccountNumber,
			String debtorBankSwift, String debtorBankAccountNumber, double total, String currency, Date dateOfValue,
			Date dateOfPayment, String paymentPurpose, String creditorInfo, String creditorAccountNumber,
			String creditorReferenceNumber, int creditorModel, String debtorInfo, String debtorAccountNumber,
			String debtorReferenceNumber, int debtorModel) {
		super();
		this.messageId = messageId;
		this.creditorBankSwift = creditorBankSwift;
		this.creditorBankAccountNumber = creditorBankAccountNumber;
		this.debtorBankSwift = debtorBankSwift;
		this.debtorBankAccountNumber = debtorBankAccountNumber;
		this.total = total;
		this.currency = currency;
		this.dateOfValue = dateOfValue;
		this.dateOfPayment = dateOfPayment;
		this.paymentPurpose = paymentPurpose;
		this.creditorInfo = creditorInfo;
		this.creditorAccountNumber = creditorAccountNumber;
		this.creditorReferenceNumber = creditorReferenceNumber;
		this.creditorModel = creditorModel;
		this.debtorInfo = debtorInfo;
		this.debtorAccountNumber = debtorAccountNumber;
		this.debtorReferenceNumber = debtorReferenceNumber;
		this.debtorModel = debtorModel;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getCreditorBankSwift() {
		return creditorBankSwift;
	}

	public void setCreditorBankSwift(String creditorBankSwift) {
		this.creditorBankSwift = creditorBankSwift;
	}

	public String getCreditorBankAccountNumber() {
		return creditorBankAccountNumber;
	}

	public void setCreditorBankAccountNumber(String creditorBankAccountNumber) {
		this.creditorBankAccountNumber = creditorBankAccountNumber;
	}

	public String getDebtorBankSwift() {
		return debtorBankSwift;
	}

	public void setDebtorBankSwift(String debtorBankSwift) {
		this.debtorBankSwift = debtorBankSwift;
	}

	public String getDebtorBankAccountNumber() {
		return debtorBankAccountNumber;
	}

	public void setDebtorBankAccountNumber(String debtorBankAccountNumber) {
		this.debtorBankAccountNumber = debtorBankAccountNumber;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public String getCreditorInfo() {
		return creditorInfo;
	}

	public void setCreditorInfo(String creditorInfo) {
		this.creditorInfo = creditorInfo;
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

	public int getCreditorModel() {
		return creditorModel;
	}

	public void setCreditorModel(int creditorModel) {
		this.creditorModel = creditorModel;
	}

	public String getDebtorInfo() {
		return debtorInfo;
	}

	public void setDebtorInfo(String debtorInfo) {
		this.debtorInfo = debtorInfo;
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

	public int getDebtorModel() {
		return debtorModel;
	}

	public void setDebtorModel(int debtorModel) {
		this.debtorModel = debtorModel;
	}
	
}

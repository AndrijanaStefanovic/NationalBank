package com.example.CentralBank.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class SinglePaymentModel {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String paymentId;

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

	@Column
	private Date dateOfOrder;

	@Column
	private double total;

	@Column
	private String currency;
	
	@ManyToOne
	private Mt102Model mt102;
	
	public SinglePaymentModel(){};

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
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

	public Date getDateOfOrder() {
		return dateOfOrder;
	}

	public void setDateOfOrder(Date dateOfOrder) {
		this.dateOfOrder = dateOfOrder;
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
	
	public Mt102Model getMt102() {
		return mt102;
	}

	public void setMt102(Mt102Model mt102) {
		this.mt102 = mt102;
	}
}

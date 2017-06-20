package com.example.Bank.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Mt102Model {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String messageId;

	@Column
	private String creditorSwift;

	@Column
	private String creditorAccountNumber;

	@Column
	private String debtorSwift;

	@Column
	private String debtorAccountNumber;

	@Column
	private double total;

	@Column
	private String currency;

	@Column
	private Date dateOfValue;

	@Column
	private Date dateOfPayment;

	@Column
	private boolean sent;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="mt102")
	private List<SinglePaymentModel> singlePaymentModels;
	
	public Mt102Model(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getCreditorSwift() {
		return creditorSwift;
	}

	public void setCreditorSwift(String creditorSwift) {
		this.creditorSwift = creditorSwift;
	}

	public String getCreditorAccountNumber() {
		return creditorAccountNumber;
	}

	public void setCreditorAccountNumber(String creditorAccountNumber) {
		this.creditorAccountNumber = creditorAccountNumber;
	}

	public String getDebtorSwift() {
		return debtorSwift;
	}

	public void setDebtorSwift(String debtorSwift) {
		this.debtorSwift = debtorSwift;
	}

	public String getDebtorAccountNumber() {
		return debtorAccountNumber;
	}

	public void setDebtorAccountNumber(String debtorAccountNumber) {
		this.debtorAccountNumber = debtorAccountNumber;
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

	public List<SinglePaymentModel> getSinglePaymentModels() {
		return singlePaymentModels;
	}

	public void setSinglePaymentModels(List<SinglePaymentModel> singlePaymentModels) {
		this.singlePaymentModels = singlePaymentModels;
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}
	
	
}

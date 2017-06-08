package com.example.Bank.model;

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
	private String paymentPurpose;
	
	public PaymentOrderModel(){}

	public PaymentOrderModel(String paymentPurpose) {
		super();
		this.paymentPurpose = paymentPurpose;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPaymentPurpose() {
		return paymentPurpose;
	}

	public void setPaymentPurpose(String paymentPurpose) {
		this.paymentPurpose = paymentPurpose;
	}
}

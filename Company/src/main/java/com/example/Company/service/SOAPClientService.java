package com.example.Company.service;

import com.example.Company.model.pojo.PaymentOrderModel;

public interface SOAPClientService {

	public String sendPaymentOrder(PaymentOrderModel paymentOrderModel);
}

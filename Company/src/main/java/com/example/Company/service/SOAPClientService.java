package com.example.Company.service;

import com.example.Company.model.pojo.PaymentOrderPojo;
import com.example.Company.service.jaxws.ProcessPaymentOrder;

public interface SOAPClientService {

	public String sendPaymentOrder(PaymentOrderPojo paymentOrderModel);

	public byte[] generateAndEncryptSessionKey();
	
	public void sendSessionKey();
	
	public ProcessPaymentOrder signWithCert(ProcessPaymentOrder ppo);
}

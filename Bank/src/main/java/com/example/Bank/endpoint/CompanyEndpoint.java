package com.example.Bank.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.example.Bank.model.BankStatementModel;
import com.example.Bank.repository.BankStatementRepository;
import com.example.Bank.service.jaxws.ProcessBankStatementRequest;
import com.example.Bank.service.jaxws.ProcessBankStatementRequestResponse;
import com.example.Bank.service.jaxws.ProcessPaymentOrder;
import com.example.Bank.service.jaxws.ProcessPaymentOrderResponse;
import com.example.service.bankstatement.BankStatement;

@Endpoint
public class CompanyEndpoint {
	private static final String NAMESPACE_URI = "http://service.Bank.example.com/";

	@Autowired
	private BankStatementRepository bankStatementRepository;


	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "processBankStatementRequest")
	@ResponsePayload
	public ProcessBankStatementRequestResponse processBankStatementRequest(@RequestPayload ProcessBankStatementRequest request) {
		ProcessBankStatementRequestResponse response = new ProcessBankStatementRequestResponse();
		BankStatement bs = new BankStatement();
		bs.setAccountNumber("1234");
		BankStatementModel bsm = new BankStatementModel("tessttt");
		bankStatementRepository.save(bsm);
		response.setReturn(bs);
		return response;
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart="processPaymentOrder")
	@ResponsePayload
	public ProcessPaymentOrderResponse processPaymentOrder(@RequestPayload ProcessPaymentOrder paymentOrder){
		ProcessPaymentOrderResponse response = new ProcessPaymentOrderResponse();
		response.setReturn(paymentOrder.getArg0().getPaymentPurpose());
		System.out.println("placa se njemu : "+paymentOrder.getArg0().getCreditor().getInfo());
		System.out.println("placa on :"+ paymentOrder.getArg0().getDebtor().getInfo());
		return response;
	}
	
	
}
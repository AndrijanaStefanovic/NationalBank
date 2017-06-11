package com.example.Bank.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.example.Bank.service.PaymentService;
import com.example.Bank.service.jaxws.ProcessBankStatementRequest;
import com.example.Bank.service.jaxws.ProcessBankStatementRequestResponse;
import com.example.Bank.service.jaxws.ProcessPaymentOrder;
import com.example.Bank.service.jaxws.ProcessPaymentOrderResponse;
import com.example.service.bankstatement.BankStatement;
import com.example.service.paymentorder.PaymentOrder;

@Endpoint
public class CompanyEndpoint {
	private static final String NAMESPACE_URI = "http://service.Bank.example.com/";
	
	@Autowired
	private PaymentService paymentService;

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "processBankStatementRequest")
	@ResponsePayload
	public ProcessBankStatementRequestResponse processBankStatementRequest(@RequestPayload ProcessBankStatementRequest request) {
		ProcessBankStatementRequestResponse response = new ProcessBankStatementRequestResponse();
		BankStatement bs = new BankStatement();
		bs.setAccountNumber("1234");
		response.setReturn(bs);
		return response;
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart="processPaymentOrder")
	@ResponsePayload
	public ProcessPaymentOrderResponse processPaymentOrder(@RequestPayload ProcessPaymentOrder processPaymentOrder){
		ProcessPaymentOrderResponse response = new ProcessPaymentOrderResponse();
		PaymentOrder paymentOrder = processPaymentOrder.getArg0();
		//Funkcija ce zaduziti racun onog koji je poslao nalog, i kreirace analitiku izvoda i azurirati dnevni balans racuna
		String code = paymentService.createAccountAnalytics(paymentOrder);
		response.setReturn(code);
		return response;
	}
	
	
}
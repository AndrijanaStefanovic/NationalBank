package com.example.Bank.endpoint;

import java.math.BigDecimal;

import com.example.Bank.service.ClearingClientService;
import com.example.Bank.service.jaxws.*;
import com.example.service.mt102.Mt102;
import com.example.service.mt103.Mt103;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.example.Bank.service.PaymentService;
import com.example.Bank.service.SOAPClientService;
import com.example.Bank.service.SecurityService;
import com.example.Bank.service.jaxws.ProcessBankStatementRequest;
import com.example.Bank.service.jaxws.ProcessBankStatementRequestResponse;
import com.example.Bank.service.jaxws.ProcessPaymentOrder;
import com.example.Bank.service.jaxws.ProcessPaymentOrderResponse;
import com.example.service.bankstatement.BankStatement;
import com.example.service.paymentorder.PaymentOrder;

@Endpoint
public class CompanyEndpoint extends WebServiceGatewaySupport {
	private static final String NAMESPACE_URI = "http://service.Bank.example.com/";
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private SecurityService securityService;

	@Autowired
	private ClearingClientService clearingClientService;

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "processBankStatementRequest")
	@ResponsePayload
	public ProcessBankStatementRequestResponse processBankStatementRequest(@RequestPayload ProcessBankStatementRequest request) {
		ProcessBankStatementRequestResponse response = new ProcessBankStatementRequestResponse();
		request.getArg0();
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
		
		boolean result = securityService.validateSignature(processPaymentOrder);
		System.out.println("Signature validation result: "+result);
		securityService.validateWithSchema(paymentOrder);
		
		String code = "";
		
		if(paymentService.checkIfClient(paymentOrder.getCreditor().getAccountNumber())){
			code = paymentService.createDebtorAccountAnalytics(paymentOrder, false);
			code = paymentService.createCreditorAccountAnalytics(paymentOrder);
		} else {
			if(paymentOrder.isUrgent() || paymentOrder.getAmount().compareTo(new BigDecimal(250000)) == 1){
				code = paymentService.createDebtorAccountAnalytics(paymentOrder, false);
				System.out.println("****************rtgs******************");
				//Mt103 mt103 = paymentService.createMT103(paymentOrder);
				//System.out.println(clientService.sendMt103(mt103));
			} else {
				code = paymentService.createDebtorAccountAnalytics(paymentOrder, true);
				System.out.println("******************clearing**********************");
				code = paymentService.createSinglePaymentForMt012(paymentOrder);
				if(code.equals("readyToSend")){
					System.out.println("sending to cb........................");
					String creditorBanksSwift = paymentService.getBanksSwift(paymentOrder.getCreditor().getAccountNumber());
					code = clearingClientService.sendMt102(creditorBanksSwift);
					System.out.println(code);
				}
			}
		}
		response.setReturn(code);
		return response;
	}
}
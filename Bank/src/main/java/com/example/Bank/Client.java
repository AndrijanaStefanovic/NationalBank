package com.example.Bank;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.example.service.CompanyService;
import com.example.service.bankstatement.BankStatement;
import com.example.service.bankstatement.BankStatement.BankStatementItem;
import com.example.service.bankstatementrequest.BankStatementRequest;
import com.example.service.paymentorder.PaymentOrder;

public class Client {

	
	public static void testProcessBankStatementRequest() throws MalformedURLException{
		
		URL wsdlLocation = new URL("http://localhost:8080/ws/company?wsdl");
		QName serviceName = new QName("http://service.example.com/", "CompanyServiceImplService");
		Service service = Service.create(wsdlLocation, serviceName);
		CompanyService requestStatementService = service.getPort(CompanyService.class);
		BankStatementRequest bsr = new BankStatementRequest();
		bsr.setAccountNumber("1111");
		BankStatement bankStatement = requestStatementService.processBankStatementRequest(bsr);
		System.out.println(bankStatement.getAccountNumber());
		for(BankStatementItem bsi : bankStatement.getBankStatementItem()){
			System.out.println(bsi.getPaymentPurpose());
		}
	}
	
	public static void testProcessPaymentOrder() throws MalformedURLException {
		URL wsdlLocation = new URL("http://localhost:8080/ws/company?wsdl");
		QName serviceName = new QName("http://service.example.com/", "CompanyServiceImplService");
		Service service = Service.create(wsdlLocation, serviceName);
		CompanyService requestStatementService = service.getPort(CompanyService.class);
		PaymentOrder po = new PaymentOrder();
		po.setMessageId("test id value");
		String response = requestStatementService.processPaymentOrder(po);
		System.out.println(response);
	}
}

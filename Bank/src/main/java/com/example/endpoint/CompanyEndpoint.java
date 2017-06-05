package com.example.endpoint;

import java.net.MalformedURLException;

import javax.xml.ws.Endpoint;

import com.example.Bank.Client;
import com.example.service.CompanyServiceImpl;

public class CompanyEndpoint {
	
	public static void main(String[] args) throws MalformedURLException {
		Endpoint.publish("http://localhost:8080/ws/company", new CompanyServiceImpl());
		
		
		Client.testProcessBankStatementRequest();
		
		//Client.testProcessPaymentOrder();

	}

}

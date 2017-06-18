package com.example.Bank;

import java.net.MalformedURLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankApplication {
	
	static {
		DisableSSlLVerification.disableSslVerification();
	}
	
	public static void main(String[] args) throws MalformedURLException {
		SpringApplication.run(BankApplication.class, args);
		Client c = new Client();
		System.out.println("Started processing 1st one");
		c.testProcessBankStatementRequest();
		System.out.println("Processed first one");
//		c.testProcessPaymentOrder();
		c.testProcessMT103();
//		c.testProcessMT900();
//		c.testProcessMT910();
		c.testProcessMT102WithCounter();
		c.testProcessMT102WithCounter();
		c.testProcessMT102WithCounter();
		c.testProcessMT102WithCounter();
//		c.testProcessMT102WithCounter();
	}
}

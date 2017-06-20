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
	}
}

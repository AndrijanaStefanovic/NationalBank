package com.example.CentralBank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CentralBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(CentralBankApplication.class, args);
		Client c = new Client();
//		c.testProcessMT103();
//		c.testProcessMT102();
	}
}

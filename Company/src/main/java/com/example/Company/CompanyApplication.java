package com.example.Company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CompanyApplication extends HttpConfiguration {

	public static void main(String[] args) {
		SpringApplication.run(CompanyApplication.class, args);
	}
}

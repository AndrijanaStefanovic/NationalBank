package com.example.Bank.service;

import com.example.service.mt103.Mt103;

public interface SOAPClientService {

	public String sendMt102(String creditorBanksSwift);
	
	public String sendMt103(Mt103 mt103);
}

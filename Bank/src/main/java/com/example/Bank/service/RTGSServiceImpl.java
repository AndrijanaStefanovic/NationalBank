package com.example.Bank.service;

import com.example.Bank.repository.BankRepository;
import com.example.service.mt103.Mt103;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RTGSServiceImpl implements RTGSService {

	@Autowired
	private BankRepository bankRepository;

	@Override
	public String processMT103(Mt103 mt103) {

		return "OK";
	}

}

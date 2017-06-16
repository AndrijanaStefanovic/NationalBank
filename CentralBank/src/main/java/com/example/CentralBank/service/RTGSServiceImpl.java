package com.example.CentralBank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CentralBank.model.Bank;
import com.example.CentralBank.repository.BankRepository;
import com.example.service.mt103.Mt103;

@Service
public class RTGSServiceImpl implements RTGSService {

	@Autowired
	private BankRepository bankRepository;
	
	@Override
	public String processMT103(Mt103 mt103) {
		
//		List<Bank> crb = bankRepository.findBySwiftCode(mt103.getCreditorsBank().getSWIFT());
//		if(crb.isEmpty()){
//			return "creditorsBankSWIFTError";
//		}
//
//		List<Bank> dbb = bankRepository.findBySwiftCode(mt103.getDebtorsBank().getSWIFT());
//		if(dbb.isEmpty()){
//			return "debtorsBankSWIFTError";
//		}
//		Bank creditorsBank = crb.get(0);
//		System.out.println(creditorsBank.getName());
//		Bank debtorsBank = dbb.get(0);
//		System.out.println(debtorsBank.getName());
		
		return "OK";
	}

}

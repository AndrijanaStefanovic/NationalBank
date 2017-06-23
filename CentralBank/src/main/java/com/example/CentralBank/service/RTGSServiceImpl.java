package com.example.CentralBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.example.CentralBank.model.Bank;
import com.example.CentralBank.repository.BankRepository;
import com.example.CentralBank.service.jaxws.ProcessMT103;
import com.example.CentralBank.service.jaxws.ProcessMT103Normal;
import com.example.CentralBank.service.jaxws.ProcessMT103ResponseNormal;
import com.example.CentralBank.service.jaxws.ProcessMT900;
import com.example.CentralBank.service.jaxws.ProcessMT900Response;
import com.example.CentralBank.service.jaxws.ProcessMT910;
import com.example.CentralBank.service.jaxws.ProcessMT910Response;
import com.example.service.mt103.Mt103;
import com.example.service.mt900.Mt900;
import com.example.service.mt910.Mt910;

@Service
public class RTGSServiceImpl extends WebServiceGatewaySupport implements RTGSService {

	@Autowired
	private BankRepository bankRepository;
	
	@Override
	public String processMT103(Mt103 mt103) {
		String creditorBankSwiftCode = mt103.getCreditorsBank().getSWIFT();
		String debtorBankSwiftCode = mt103.getDebtorsBank().getSWIFT();
		
		Bank creditorBank = bankRepository.findBySwiftCode(creditorBankSwiftCode).get(0);
		Bank debtorBank = bankRepository.findBySwiftCode(debtorBankSwiftCode).get(0);
		
		creditorBank.calculateAccountBalance(-mt103.getTotal().doubleValue());
		bankRepository.save(creditorBank);
		
		System.out.println("Creditor bank:" + creditorBank.getAccountBalance());
		
		debtorBank.calculateAccountBalance(mt103.getTotal().doubleValue());
		bankRepository.save(debtorBank);
		
		System.out.println("Debtor bank:" + debtorBank.getAccountBalance());
		
		return "OK";
		
	}

	@Override
	public String sendMT900(Mt103 mt103) {
		Mt900 mt900 = new Mt900();
			
		com.example.service.mt900.TBankData debtorsBank = new com.example.service.mt900.TBankData();
		debtorsBank.setAccountNumber(mt103.getDebtorsBank().getAccountNumber());
		debtorsBank.setSWIFT(mt103.getDebtorsBank().getSWIFT());
		
		mt900.setDebtorsBank(debtorsBank);		
		mt900.setMessageId(mt103.getMessageId());		
		mt900.setOrderMessageId("MT103");		
		mt900.setTotal(mt103.getTotal());
		mt900.setCurrency(mt103.getCurrency());	
		mt900.setDateOfValue(mt103.getDateOfValue());
		
		return forwardMT900(mt900);
	}
	
	@Override
	public String sendMT910(Mt103 mt103) {
		Mt910 mt910 = new Mt910();
		
		com.example.service.mt910.TBankData creditorsBank = new com.example.service.mt910.TBankData();
		creditorsBank.setAccountNumber(mt103.getCreditorsBank().getAccountNumber());
		creditorsBank.setSWIFT(mt103.getCreditorsBank().getSWIFT());
		
		mt910.setCreditorsBank(creditorsBank);		
		mt910.setMessageId(mt103.getMessageId());		
		mt910.setOrderMessageId("RTGS");		
		mt910.setTotal(mt103.getTotal());
		mt910.setCurrency(mt103.getCurrency());	
		mt910.setDateOfValue(mt103.getDateOfValue());
		
		return forwardMT910(mt910);
	}
	
	public String forwardMT900(Mt900 mt900) {
		System.out.println("u forward mt900");
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(ProcessMT900.class, ProcessMT900Response.class);
		setMarshaller(marshaller);
		setUnmarshaller(marshaller);
		
		ProcessMT900 processMT900 = new ProcessMT900();
		processMT900.setArg0(mt900);
		Bank debtorBank = bankRepository.findBySwiftCode(mt900.getDebtorsBank().getSWIFT()).get(0);
		System.out.println("found debtors bank:");
		System.out.println(debtorBank.getName());
		String url = debtorBank.getUrl() + "mt900";
		Object o = getWebServiceTemplate().marshalSendAndReceive(url, processMT900);
		ProcessMT900Response response = (ProcessMT900Response) o;
		System.out.println(response.getReturn());
		
		return "OK";
	}
	
	public String forwardMT910(Mt910 mt910) {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(ProcessMT910.class, ProcessMT910Response.class);
		setMarshaller(marshaller);
		setUnmarshaller(marshaller);
		
		ProcessMT910 processMT910 = new ProcessMT910();
		processMT910.setArg0(mt910);
		Bank creditorBank = bankRepository.findBySwiftCode(mt910.getCreditorsBank().getSWIFT()).get(0);
		String url = creditorBank.getUrl() + "mt910";
		Object o = getWebServiceTemplate().marshalSendAndReceive(url, processMT910);
		ProcessMT910Response response = (ProcessMT910Response) o;
		System.out.println(response.getReturn());
		
		return "OK";
	}

	@Override
	public void forwardMT103(Mt103 mt103) {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(ProcessMT103Normal.class, ProcessMT103ResponseNormal.class);
		setMarshaller(marshaller);
		setUnmarshaller(marshaller);
		
		ProcessMT103Normal processMT103 = new ProcessMT103Normal();
		processMT103.setArg0(mt103);
		Bank creditorBank = bankRepository.findBySwiftCode(mt103.getCreditorsBank().getSWIFT()).get(0);
		String url = creditorBank.getUrl() + "mt103";
		Object o = getWebServiceTemplate().marshalSendAndReceive(url, processMT103);
		ProcessMT103ResponseNormal response = (ProcessMT103ResponseNormal) o;
		System.out.println(response.getReturn());
		
	}

}

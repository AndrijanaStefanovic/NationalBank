package com.example.CentralBank.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.example.CentralBank.model.Bank;
import com.example.CentralBank.model.Mt102Model;
import com.example.CentralBank.model.SinglePaymentModel;
import com.example.CentralBank.repository.BankRepository;
import com.example.CentralBank.repository.Mt102Repository;
import com.example.CentralBank.repository.SinglePaymentRepository;
import com.example.CentralBank.service.jaxws.ProcessMT102Normal;
import com.example.CentralBank.service.jaxws.ProcessMT102ResponseNormal;
import com.example.CentralBank.service.jaxws.ProcessMT900;
import com.example.CentralBank.service.jaxws.ProcessMT900Response;
import com.example.CentralBank.service.jaxws.ProcessMT910;
import com.example.CentralBank.service.jaxws.ProcessMT910Response;
import com.example.service.mt102.Mt102;
import com.example.service.mt102.SinglePayment;
import com.example.service.mt900.Mt900;
import com.example.service.mt910.Mt910;

@Service
public class ClearingServiceImpl extends WebServiceGatewaySupport implements ClearingService{

    @Autowired
    private BankRepository bankRepository;
    
    @Autowired
    private SinglePaymentRepository singlePaymentRepository;
    
    @Autowired
    private Mt102Repository mt102Repository;

    @Override
    public String processMT102(Mt102 mt102) {
    	System.out.println("processing mt102 with uuid:"+mt102.getMessageId());
        List<Bank> crb = bankRepository.findBySwiftCode(mt102.getCreditorSwift());
        if(crb.isEmpty()){
            return "creditorsBankSWIFTError";
        }

        List<Bank> dbb = bankRepository.findBySwiftCode(mt102.getDebtorSwift());
        if(dbb.isEmpty()){
            return "debtorsBankSWIFTError";
        }
        Mt102Model mt102Model = new Mt102Model();
        mt102Model.setCreditorAccountNumber(mt102.getCreditorAccountNumber());
        mt102Model.setCleared(false);
        mt102Model.setCreditorSwift(mt102.getCreditorSwift());
        mt102Model.setDebtorAccountNumber(mt102.getDebtorAccountNumber());
        mt102Model.setDebtorSwift(mt102.getDebtorSwift());
        mt102Model.setCurrency(mt102.getCurrency());
        mt102Model.setDateOfValue(mt102.getDateOfValue().toGregorianCalendar().getTime());
        mt102Model.setDateOfPayment(mt102.getDateOfPayment().toGregorianCalendar().getTime());
        mt102Model.setTotal(mt102.getTotal().doubleValue());
        mt102Model.setMessageId(mt102.getMessageId());
        mt102Repository.save(mt102Model);

        ArrayList<SinglePaymentModel> spmList = new ArrayList<SinglePaymentModel>();
        for(SinglePayment sp : mt102.getSinglePayment()){
        	SinglePaymentModel spm = new SinglePaymentModel();
        	spm.setCreditorAccountNumber(sp.getCreditor().getAccountNumber());
        	spm.setCreditorInfo(sp.getCreditor().getInfo());
        	spm.setCreditorModel(sp.getCreditor().getModel());
        	spm.setCreditorReferenceNumber(sp.getCreditor().getReferenceNumber());
        	spm.setCurrency(sp.getCurrency());
        	spm.setDateOfOrder(sp.getDateOfOrder().toGregorianCalendar().getTime());
        	spm.setDebtorAccountNumber(sp.getDebtor().getAccountNumber());
        	spm.setDebtorInfo(sp.getDebtor().getInfo());
        	spm.setDebtorModel(sp.getDebtor().getModel());
        	spm.setDebtorReferenceNumber(sp.getDebtor().getReferenceNumber());
        	spm.setMt102(mt102Model);
        	spm.setPaymentPurpose(sp.getPaymentPurpose());
        	spm.setTotal(sp.getTotal().doubleValue());
        	SinglePaymentModel spmDB = singlePaymentRepository.save(spm);
        	spmList.add(spmDB);
        	
        }
        
        mt102Model.setSinglePaymentModels(spmList);
        mt102Repository.save(mt102Model);
        
        List<Mt102Model> mt102List = mt102Repository.findByCleared(false);
        if(mt102List.size() >= 2){
        	return "readyToClear";
        }
        return "OK";
    }

	@Override
	public String performClear() {
		System.out.println("performing clearing...");
		List<Mt102Model> unclearedMt102s = mt102Repository.findByCleared(false);
		for(Mt102Model mt102Model : unclearedMt102s){
			List<Bank> creditorBankList = bankRepository.findBySwiftCode(mt102Model.getCreditorSwift());
			if(creditorBankList.isEmpty()){
				return "creditorBankNotFound";
			}
			Bank creditorBank = creditorBankList.get(0);
			List<Bank> debtorBankList = bankRepository.findBySwiftCode(mt102Model.getDebtorSwift());
			if(debtorBankList.isEmpty()){
				return "debtorBankNotFound";
			}
			Bank debtorBank = debtorBankList.get(0);
			
			debtorBank.setAccountBalance(debtorBank.getAccountBalance()-mt102Model.getTotal());
			creditorBank.setAccountBalance(creditorBank.getAccountBalance()+mt102Model.getTotal());
			bankRepository.save(debtorBank);
			bankRepository.save(creditorBank);
		
			//forwardMt102(mt102Model);
			System.out.println("sending mt900..");
			sendMt900(mt102Model);
			
			System.out.println("done with debtor..on to the creditor...");
			System.out.println("sending mt102-----------------");
			forwardMt102(mt102Model);
			System.out.println("sending mt910----------------");
			sendMt910(mt102Model);
			//posalji mt900 debtor banci
			//prosledi mt102 creditor banci
			//posalji mt910 creditor banci
			
			mt102Model.setCleared(true);
			mt102Repository.save(mt102Model);
		}
		return "OK";
	}

	@Override
	public String forwardMt102(Mt102Model mt102model) {
		List<Bank> creditorBankList = bankRepository.findBySwiftCode(mt102model.getCreditorSwift());
		if(creditorBankList.isEmpty()){
			return "creditorBankNotFound";
		}
		Bank creditorBank = creditorBankList.get(0);
		System.out.println("forwarding mt102 with uuid:"+mt102model.getMessageId());
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(ProcessMT102Normal.class, ProcessMT102ResponseNormal.class);
		setMarshaller(marshaller);
		setUnmarshaller(marshaller);

		Mt102 mt102 = new Mt102();
		mt102.setMessageId(mt102model.getMessageId());
		mt102.setCreditorAccountNumber(mt102model.getCreditorAccountNumber());
		mt102.setCreditorSwift(mt102model.getCreditorSwift());
		mt102.setDebtorAccountNumber(mt102model.getDebtorAccountNumber());
		mt102.setDebtorSwift(mt102model.getDebtorSwift());
		
		try {
			GregorianCalendar c = new GregorianCalendar();
	    	c.setTime(mt102model.getDateOfValue());
	    	XMLGregorianCalendar dateOfValue = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	    	mt102.setDateOfValue(dateOfValue);
			c.setTime(mt102model.getDateOfPayment());
	    	XMLGregorianCalendar dateOfPayment = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	    	mt102.setDateOfPayment(dateOfPayment);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		
		mt102.setCurrency(mt102model.getCurrency());
		mt102.setTotal(new BigDecimal(mt102model.getTotal()));
		ArrayList<SinglePayment> singlePayments = new ArrayList<SinglePayment>();
		
		for(SinglePaymentModel spm : mt102model.getSinglePaymentModels()){
			SinglePayment sp = new SinglePayment();
			com.example.service.mt102.TCompanyData creditorBankData = new com.example.service.mt102.TCompanyData();
			creditorBankData.setAccountNumber(spm.getCreditorAccountNumber());
			creditorBankData.setInfo(spm.getCreditorInfo());
			creditorBankData.setModel(spm.getCreditorModel());
			creditorBankData.setReferenceNumber(spm.getCreditorReferenceNumber());
			sp.setCreditor(creditorBankData);
			
			com.example.service.mt102.TCompanyData debtorBankData = new com.example.service.mt102.TCompanyData();
			debtorBankData.setAccountNumber(spm.getDebtorAccountNumber());
			debtorBankData.setInfo(spm.getDebtorInfo());
			debtorBankData.setModel(spm.getDebtorModel());
			debtorBankData.setReferenceNumber(spm.getDebtorReferenceNumber());
			sp.setDebtor(debtorBankData);
			
			
			try {
				GregorianCalendar c = new GregorianCalendar();
		    	c.setTime(spm.getDateOfOrder());
		    	XMLGregorianCalendar dateOfOrder = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		    	sp.setDateOfOrder(dateOfOrder);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
			sp.setCurrency(spm.getCurrency());
			sp.setPaymentId("?");
			sp.setPaymentPurpose(spm.getPaymentPurpose());
			sp.setTotal(new BigDecimal(spm.getTotal()));
			singlePayments.add(sp);
		}
		
		mt102.setSinglePayment(singlePayments);
		
		System.out.println("sending mt102...");
		ProcessMT102Normal p = new ProcessMT102Normal();
		p.setArg0(mt102);
		String uri = creditorBank.getUrl() + "mt102";
		Object o = getWebServiceTemplate().marshalSendAndReceive(uri, p);
		ProcessMT102ResponseNormal response = (ProcessMT102ResponseNormal) o;
		System.out.println(response.getReturn());
		return "OK";
	}

	@Override
	public String sendMt900(Mt102Model mt102model) {
		System.out.println("sending mt900 with uuid:"+mt102model.getMessageId());
		Mt900 mt900 = new Mt900();
		mt900.setCurrency(mt102model.getCurrency());
		try {
			GregorianCalendar c = new GregorianCalendar();
	    	c.setTime(mt102model.getDateOfValue());
	    	XMLGregorianCalendar dateOfValue = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	    	mt900.setDateOfValue(dateOfValue);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		com.example.service.mt900.TBankData debtorsBankData = new com.example.service.mt900.TBankData();
		debtorsBankData.setAccountNumber(mt102model.getDebtorAccountNumber());
		debtorsBankData.setSWIFT(mt102model.getDebtorSwift());
		mt900.setDebtorsBank(debtorsBankData);
		mt900.setMessageId(mt102model.getMessageId());
		mt900.setOrderMessageId(mt102model.getId().toString());
		mt900.setTotal(new BigDecimal(mt102model.getTotal()));
		
		ProcessMT900 p = new ProcessMT900();
		p.setArg0(mt900);

		List<Bank> debtorsBankList = bankRepository.findBySwiftCode(mt102model.getDebtorSwift());
		if(debtorsBankList.isEmpty()){
			return "debtorsBankNotFound";
		}
		System.out.println("sending mt900 to debtors bank.....");
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(ProcessMT900.class, ProcessMT900Response.class);
		setMarshaller(marshaller);
		setUnmarshaller(marshaller);
		Bank debtorsBank = debtorsBankList.get(0);
		String uri = debtorsBank.getUrl() + "mt900";
		Object o = getWebServiceTemplate().marshalSendAndReceive(uri, p);
		ProcessMT900Response response = (ProcessMT900Response) o;
		return response.getReturn();
	}

	@Override
	public String sendMt910(Mt102Model mt102model) {
		System.out.println("sending mt910 with uuid:"+mt102model.getMessageId());
		Mt910 mt910 = new Mt910();
		com.example.service.mt910.TBankData creditorsBankData = new com.example.service.mt910.TBankData();
		creditorsBankData.setAccountNumber(mt102model.getCreditorAccountNumber());
		creditorsBankData.setSWIFT(mt102model.getCreditorSwift());
		mt910.setCreditorsBank(creditorsBankData);
		mt910.setCurrency(mt102model.getCurrency());
		try {
			GregorianCalendar c = new GregorianCalendar();
	    	c.setTime(mt102model.getDateOfValue());
	    	XMLGregorianCalendar dateOfValue = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	    	mt910.setDateOfValue(dateOfValue);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		mt910.setMessageId(mt102model.getMessageId());
		mt910.setOrderMessageId(mt102model.getId().toString());
		mt910.setTotal(new BigDecimal(mt102model.getTotal()));
		
		List<Bank> creditorsBankList = bankRepository.findBySwiftCode(mt102model.getCreditorSwift());
		if(creditorsBankList.isEmpty()){
			return "creditorsBankNotFound";
		}
		Bank creditorsBank = creditorsBankList.get(0);
		
		System.out.println("sending mt910 to creditors bank...");
		ProcessMT910 processMt910 = new ProcessMT910();
		processMt910.setArg0(mt910);
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(ProcessMT910.class, ProcessMT910Response.class);
		setMarshaller(marshaller);
		setUnmarshaller(marshaller);
		String uri = creditorsBank.getUrl() + "mt910";
		Object o = getWebServiceTemplate().marshalSendAndReceive(uri, processMt910);
		ProcessMT910Response response = (ProcessMT910Response) o;
		return response.getReturn();
	}
}

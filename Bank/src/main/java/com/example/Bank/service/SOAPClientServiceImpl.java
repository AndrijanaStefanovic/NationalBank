package com.example.Bank.service;

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

import com.example.Bank.model.Mt102Model;
import com.example.Bank.model.SinglePaymentModel;
import com.example.Bank.repository.Mt102Repository;
import com.example.Bank.service.jaxws.ProcessMT102Central;
import com.example.Bank.service.jaxws.ProcessMT102ResponseCentral;
import com.example.Bank.service.jaxws.ProcessMT103;
import com.example.Bank.service.jaxws.ProcessMT103Response;
import com.example.service.mt102.Mt102;
import com.example.service.mt102.SinglePayment;
import com.example.service.mt103.Mt103;

@Service
public class SOAPClientServiceImpl extends WebServiceGatewaySupport  implements SOAPClientService{

	@Autowired
	private Mt102Repository mt102Repository;
	
    @Override
    public String sendMt102(String creditorBanksSwift) {
    	List<Mt102Model> mt102Models = mt102Repository.findByCreditorSwiftAndSent(creditorBanksSwift, false);
    	if(mt102Models.isEmpty()){
    		return "mt102NotFound";
    	}
    	Mt102Model mt102Model = mt102Models.get(0);
    	
    	Mt102  mt102 = new Mt102();
    	mt102.setCreditorAccountNumber(mt102Model.getCreditorAccountNumber());
    	mt102.setCreditorSwift(mt102Model.getCreditorSwift());
    	mt102.setCurrency(mt102Model.getCurrency());
    	
    	try {
			GregorianCalendar c = new GregorianCalendar();
	    	c.setTime(mt102Model.getDateOfPayment());
	    	XMLGregorianCalendar dateOfPayment = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			c.setTime(mt102Model.getDateOfValue());
	    	XMLGregorianCalendar dateOfValue = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	    	mt102.setDateOfPayment(dateOfPayment);
	    	mt102.setDateOfValue(dateOfValue);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
    	
    	mt102.setDebtorAccountNumber(mt102Model.getDebtorAccountNumber());
    	mt102.setDebtorSwift(mt102Model.getDebtorSwift());
    	mt102.setMessageId(mt102Model.getId().toString());
    	mt102.setTotal(new BigDecimal(mt102Model.getTotal()));
    	
    	ArrayList<SinglePayment> spList = new ArrayList<SinglePayment>();
    	for(SinglePaymentModel spm : mt102Model.getSinglePaymentModels()){
    		SinglePayment sp = new SinglePayment();
    		com.example.service.mt102.TCompanyData creditorData = new com.example.service.mt102.TCompanyData();
    		creditorData.setAccountNumber(spm.getCreditorAccountNumber());
    		creditorData.setInfo(spm.getCreditorInfo());
    		creditorData.setModel(spm.getCreditorModel());
    		creditorData.setReferenceNumber(spm.getCreditorReferenceNumber());
    		sp.setCreditor(creditorData);
    		
    		com.example.service.mt102.TCompanyData debtorData = new com.example.service.mt102.TCompanyData();
    		debtorData.setAccountNumber(spm.getDebtorAccountNumber());
    		debtorData.setInfo(spm.getDebtorInfo());
    		debtorData.setModel(spm.getDebtorModel());
    		debtorData.setReferenceNumber(spm.getDebtorReferenceNumber());
    		sp.setDebtor(debtorData);
    		
    		sp.setCurrency(spm.getCurrency());
    		sp.setPaymentId("...");
    		sp.setPaymentPurpose(spm.getPaymentPurpose());
    		sp.setTotal(new BigDecimal(spm.getTotal()));
    		
			try {
				GregorianCalendar c = new GregorianCalendar();
		    	c.setTime(spm.getDateOfOrder());
		    	XMLGregorianCalendar dateOfOrder = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
				sp.setDateOfOrder(dateOfOrder);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
	    	
    		spList.add(sp);
    	}
    	mt102.setSinglePayment(spList);
    	
    	mt102Model.setSent(true);
		mt102Repository.save(mt102Model);
    	
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(ProcessMT102Central.class, ProcessMT102ResponseCentral.class);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

        ProcessMT102Central processMt102Central = new ProcessMT102Central();
        processMt102Central.setArg0(mt102);

        String uri = "https://localhost:8090/ws/mt102";
        Object o = getWebServiceTemplate().marshalSendAndReceive(uri, processMt102Central);

        ProcessMT102ResponseCentral response = (ProcessMT102ResponseCentral)o;
        return response.getReturn();
    }
	
	@Override
	public String sendMt103(Mt103 mt103) {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(ProcessMT103.class, ProcessMT103Response.class);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

        ProcessMT103 processMt103 = new ProcessMT103();
        processMt103.setArg0(mt103);

        String uri = "https://localhost:8090/ws/mt103";
        Object o = getWebServiceTemplate().marshalSendAndReceive(uri, processMt103);
        
        ProcessMT103Response response = (ProcessMT103Response)o;
		return response.getReturn();
	}

}

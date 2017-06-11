package com.example.Company.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.example.Company.model.Invoice;
import com.example.Company.model.pojo.PaymentOrderModel;
import com.example.Company.repository.InvoiceRepository;
import com.example.Company.service.jaxws.ProcessPaymentOrder;
import com.example.Company.service.jaxws.ProcessPaymentOrderResponse;
import com.example.service.paymentorder.PaymentOrder;
import com.example.service.paymentorder.TCompanyData;

@Service
public class SOAPClientServiceImpl extends WebServiceGatewaySupport implements SOAPClientService {

	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Override
	public String sendPaymentOrder(PaymentOrderModel paymentOrderModel) {
		Invoice invoice = invoiceRepository.findOne(paymentOrderModel.getInvoiceId());
		double totalDue = invoice.getTotalDue() - paymentOrderModel.getAmount();
		if(totalDue < 0){
			totalDue = 0;
		}
		invoice.setTotalDue(totalDue);
		invoiceRepository.save(invoice);
		
		PaymentOrder paymentOrder = new PaymentOrder();
		paymentOrder.setAmount(new BigDecimal(paymentOrderModel.getAmount()));
		paymentOrder.setUrgent(paymentOrderModel.getUrgent());
		paymentOrder.setPaymentPurpose("Payment based on an invoice");
		paymentOrder.setCurrency(invoice.getCurrency());
		paymentOrder.setMessageId("?");
		try {
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(invoice.getDateOfValue());
			XMLGregorianCalendar dateOfValue = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			c.setTime(new Date());
			XMLGregorianCalendar dateOfPayment = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			paymentOrder.setDateOfValue(dateOfValue);
			paymentOrder.setDateOfPayment(dateOfPayment);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		
		TCompanyData debtorData = new TCompanyData();
		debtorData.setInfo(invoice.getBuyerName() + " " + invoice.getBuyerAddress());
		debtorData.setAccountNumber("123");
		debtorData.setModel(97);
		debtorData.setReferenceNumber("111");
		TCompanyData creditorData = new TCompanyData();
		creditorData.setInfo(invoice.getSupplierName() + " " + invoice.getSupplierAddress());
		creditorData.setAccountNumber("321");
		creditorData.setModel(97);
		creditorData.setReferenceNumber("111");
		
		paymentOrder.setCreditor(creditorData);
		paymentOrder.setDebtor(debtorData);
		
		ProcessPaymentOrder ppo = new ProcessPaymentOrder();
		ppo.setArg0(paymentOrder);
		
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
	    marshaller.setClassesToBeBound(ProcessPaymentOrder.class, ProcessPaymentOrderResponse.class);
	    setMarshaller(marshaller);
	    setUnmarshaller(marshaller);

		String uri = "http://localhost:8080/ws/paymentorder";
		Object o = getWebServiceTemplate().marshalSendAndReceive(uri, ppo);
		ProcessPaymentOrderResponse response = (ProcessPaymentOrderResponse) o;
		return response.getReturn();
	}

}

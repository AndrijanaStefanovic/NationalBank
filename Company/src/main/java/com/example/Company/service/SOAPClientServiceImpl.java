package com.example.Company.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.example.Company.model.BusinessPartner;
import com.example.Company.model.Company;
import com.example.Company.model.Invoice;
import com.example.Company.model.pojo.PaymentOrderModel;
import com.example.Company.repository.BusinessPartnerRepository;
import com.example.Company.repository.CompanyRepository;
import com.example.Company.repository.InvoiceRepository;
import com.example.Company.service.jaxws.ProcessPaymentOrder;
import com.example.Company.service.jaxws.ProcessPaymentOrderResponse;
import com.example.service.paymentorder.PaymentOrder;
import com.example.service.paymentorder.TCompanyData;

@Service
public class SOAPClientServiceImpl extends WebServiceGatewaySupport implements SOAPClientService {

	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private BusinessPartnerRepository businessPartnerRepository;
	
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
		
		List<Company> debtorList = companyRepository.findByCompanyPIB(invoice.getBuyerPIB());
		if(debtorList.isEmpty()){
			return "CompanyPIBError";
		}
		List<BusinessPartner> creditorList = businessPartnerRepository.findByPartnerPIB(invoice.getSupplierPIB());
		if(creditorList.isEmpty()){
			return "BusinessPartnerPIBError";
		}
		Company debtor = debtorList.get(0);
		BusinessPartner creditor =  creditorList.get(0);
		
		TCompanyData debtorData = new TCompanyData();
		debtorData.setInfo(debtor.getName() + " " + debtor.getCompanyAddress());
		debtorData.setAccountNumber(debtor.getCompanyAccount());
		debtorData.setModel(Integer.parseInt(debtor.getModel()));
		debtorData.setReferenceNumber(debtor.getReferenceNumber());
		
		TCompanyData creditorData = new TCompanyData();
		creditorData.setInfo(creditor.getName() + " " + creditor.getPartnerAddress());
		creditorData.setAccountNumber(creditor.getPartnerAccount());
		creditorData.setModel(Integer.parseInt(creditor.getModel()));
		creditorData.setReferenceNumber(creditor.getReferenceNumber());
		
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

package com.example.Bank;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.example.Bank.service.jaxws.ProcessBankStatementRequest;
import com.example.Bank.service.jaxws.ProcessBankStatementRequestResponse;
import com.example.Bank.service.jaxws.ProcessPaymentOrder;
import com.example.Bank.service.jaxws.ProcessPaymentOrderResponse;
import com.example.service.bankstatementrequest.BankStatementRequest;
import com.example.service.paymentorder.PaymentOrder;

public class Client extends WebServiceGatewaySupport{

	
	public void testProcessBankStatementRequest() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(ProcessBankStatementRequest.class, ProcessBankStatementRequestResponse.class);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

        ProcessBankStatementRequest p = new ProcessBankStatementRequest();
		BankStatementRequest b = new BankStatementRequest();
		b.setAccountNumber("test");
		p.setArg0(b);

        String uri = "http://localhost:8080/ws/bankstatement";
        Object o = getWebServiceTemplate().marshalSendAndReceive(uri, p);
        ProcessBankStatementRequestResponse response = (ProcessBankStatementRequestResponse)o;
        System.out.println("**************************************");
        System.out.println(response.getReturn().getAccountNumber());
    }
	
	public void testProcessPaymentOrder(){
		  Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
	      marshaller.setClassesToBeBound(ProcessPaymentOrder.class, ProcessPaymentOrderResponse.class);
	      setMarshaller(marshaller);
	      setUnmarshaller(marshaller);
	      
	      ProcessPaymentOrder ppo = new ProcessPaymentOrder();
	      PaymentOrder po = new PaymentOrder();
	      po.setMessageId("vrednost poruke");
	      ppo.setArg0(po);
	      String uri = "http://localhost:8080/ws/paymentorder";
	      Object o = getWebServiceTemplate().marshalSendAndReceive(uri, ppo);
	      ProcessPaymentOrderResponse response = (ProcessPaymentOrderResponse)o;
	      System.out.println("*******************************");
	      System.out.println(response.getReturn());
	}
	

}

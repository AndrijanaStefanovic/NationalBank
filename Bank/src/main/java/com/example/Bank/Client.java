package com.example.Bank;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.example.Bank.service.jaxws.ProcessBankStatementRequest;
import com.example.Bank.service.jaxws.ProcessBankStatementRequestResponse;
import com.example.Bank.service.jaxws.ProcessMT103;
import com.example.Bank.service.jaxws.ProcessMT103Response;
import com.example.Bank.service.jaxws.ProcessPaymentOrder;
import com.example.Bank.service.jaxws.ProcessPaymentOrderResponse;
import com.example.service.bankstatementrequest.BankStatementRequest;
import com.example.service.mt103.Mt103;
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

        String uri = "https://localhost:8080/ws/bankstatement";
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
	      String uri = "https://localhost:8080/ws/paymentorder";
	      Object o = getWebServiceTemplate().marshalSendAndReceive(uri, ppo);
	      ProcessPaymentOrderResponse response = (ProcessPaymentOrderResponse)o;
	      System.out.println("*******************************");
	      System.out.println(response.getReturn());
	}
	
	public void testProcessMT103() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(ProcessMT103.class, ProcessMT103Response.class);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

        ProcessMT103 p = new ProcessMT103();
		Mt103 m = new Mt103();
		m.setMessageId("testiram soap cb");
		p.setArg0(m);

        String uri = "https://localhost:8090/ws/mt103";
        Object o = getWebServiceTemplate().marshalSendAndReceive(uri, p);
        ProcessMT103Response response = (ProcessMT103Response)o;
        System.out.println("**************************************");
        System.out.println(response.getReturn());
    }
	

}

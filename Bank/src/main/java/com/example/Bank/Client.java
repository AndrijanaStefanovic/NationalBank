package com.example.Bank;

import com.example.Bank.model.ClearingCounter;
import com.example.Bank.repository.ClearingCounterRepository;
import com.example.Bank.service.PaymentService;
import com.example.Bank.service.jaxws.*;
import com.example.service.mt102.Mt102;
import com.example.service.mt102.SinglePayment;
import com.example.service.mt900.Mt900;
import com.example.service.mt910.Mt910;
import com.example.service.paymentorder.TCompanyData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.example.service.bankstatementrequest.BankStatementRequest;
import com.example.service.mt103.Mt103;
import com.example.service.paymentorder.PaymentOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Client extends WebServiceGatewaySupport {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ClearingCounterRepository clearingCounterRepository;

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
        ProcessBankStatementRequestResponse response = (ProcessBankStatementRequestResponse) o;
        System.out.println("**************************************");
        System.out.println(response.getReturn().getAccountNumber());
    }

    public void testProcessPaymentOrder() {
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
        ProcessPaymentOrderResponse response = (ProcessPaymentOrderResponse) o;
        System.out.println("*******************************");
        System.out.println(response.getReturn());
    }

    public void testProcessMT103() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(ProcessMT103Central.class, ProcessMT103ResponseCentral.class);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

        ProcessMT103Central p = new ProcessMT103Central();
        Mt103 m = new Mt103();
        m.setMessageId("testiram soap cb");
        p.setArg0(m);

        String uri = "http://localhost:8090/ws/mt103";
        Object o = getWebServiceTemplate().marshalSendAndReceive(uri, p);
        ProcessMT103ResponseCentral response = (ProcessMT103ResponseCentral) o;
        System.out.println("**************************************");
        System.out.println(response.getReturn());
    }

    int i = 0;

    public void testProcessMT102WithCounter(){
        i++;
        System.out.println("Number of times i entered ___ " + i);
        if (i % 5 == 0) {
            for (int i = 0; i<this.i; i++) {
                testProcessMT102();
            }
        }
    }

    public void testProcessMT102() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(ProcessMT102Central.class, ProcessMT102ResponseCentral.class);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

        ProcessMT102Central p = new ProcessMT102Central();
        Mt102 m = new Mt102();
        List<SinglePayment> singlePayments = m.getSinglePayment();
        for (int i = 0; i < 5; i++) {
            singlePayments.add(generateSinglePaymentForCurrentMT102(i));
        }
        m.setMessageId("testiram soap za mt102 clearing");
        p.setArg0(m);

        String uri = "http://localhost:8090/ws/mt102";
        Object o = getWebServiceTemplate().marshalSendAndReceive(uri, p);
        ProcessMT102ResponseCentral response = (ProcessMT102ResponseCentral) o;
        System.out.println("**************************************");
        System.out.println(response.getReturn());
    }

    private SinglePayment generateSinglePaymentForCurrentMT102(int i) {
        SinglePayment singlePayment = new SinglePayment();

        //paymentID
        singlePayment.setPaymentId(i+"ID"+ UUID.randomUUID().toString());

        //creditor
        com.example.service.mt102.TCompanyData creditorData = new com.example.service.mt102.TCompanyData();
        singlePayment.setCreditor(creditorData);

        //debtor
        com.example.service.mt102.TCompanyData debtorData = new com.example.service.mt102.TCompanyData();
        singlePayment.setCreditor(debtorData);

        return  singlePayment;
    }

    public void testProcessMT900() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(ProcessMT900.class, ProcessMT900Response.class);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

        ProcessMT900 p = new ProcessMT900();
        Mt900 m = new Mt900();
        m.setMessageId("testiram soap za mt900");
        p.setArg0(m);

        String uri = "http://localhost:8080/ws/mt900";
        Object o = getWebServiceTemplate().marshalSendAndReceive(uri, p);
        ProcessMT900Response response = (ProcessMT900Response) o;
        System.out.println("**************************************");
        System.out.println(response.getReturn());
    }

    public void testProcessMT910() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(ProcessMT910.class, ProcessMT910Response.class);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

        ProcessMT910 p = new ProcessMT910();
        Mt910 m = new Mt910();
        m.setMessageId("testiram soap za mt910");
        p.setArg0(m);

        String uri = "http://localhost:8080/ws/mt910";
        Object o = getWebServiceTemplate().marshalSendAndReceive(uri, p);
        ProcessMT910Response response = (ProcessMT910Response) o;
        System.out.println("**************************************");
        System.out.println(response.getReturn());
    }
}

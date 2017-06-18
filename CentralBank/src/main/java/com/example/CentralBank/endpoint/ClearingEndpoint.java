package com.example.CentralBank.endpoint;

import com.example.CentralBank.service.ClearingService;
import com.example.CentralBank.service.ResponseService;
import com.example.CentralBank.service.jaxws.*;
import com.example.service.mt102.Mt102;
import com.example.service.mt102.SinglePayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class ClearingEndpoint extends WebServiceGatewaySupport {

    private static final String NAMESPACE_URI = "http://service.CentralBank.example.com/";

    @Autowired
    private ClearingService clearingService;

    @Autowired
    private ResponseService responseService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "processMT102")
    @ResponsePayload
    public ProcessMT102Response processMT102(@RequestPayload ProcessMT102 mt102) {
        System.out.println(mt102.getArg0().getMessageId());
        for(SinglePayment singlePayment : mt102.getArg0().getSinglePayment()) {
            System.out.println(singlePayment.getPaymentId());
        }
        ProcessMT102Response r = new ProcessMT102Response();
        r.setReturn("test return mt102");
        clearingService.processMT102(mt102.getArg0());

        forwardMT102();

        responseService.sendResponseMT900();

        responseService.sendResponseMT910();

        return r;
    }

    private void forwardMT102() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(ProcessMT102Normal.class, ProcessMT102ResponseNormal.class);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

        ProcessMT102Normal p = new ProcessMT102Normal();
        Mt102 m = new Mt102();
        m.setMessageId("testiram forward soap-a za mt102 na normalnu banku");
        p.setArg0(m);

        String uri = "https://localhost:8080/ws/mt102";
        Object o = getWebServiceTemplate().marshalSendAndReceive(uri, p);
        ProcessMT102ResponseNormal response = (ProcessMT102ResponseNormal)o;
        System.out.println("**************************************");
        System.out.println(response.getReturn());
    }
}

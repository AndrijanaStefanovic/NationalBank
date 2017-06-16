package com.example.CentralBank.endpoint;

import com.example.CentralBank.service.ClearingService;
import com.example.CentralBank.service.jaxws.ProcessMT102;
import com.example.CentralBank.service.jaxws.ProcessMT102Response;
import com.example.CentralBank.service.jaxws.ProcessMT900;
import com.example.CentralBank.service.jaxws.ProcessMT900Response;
import com.example.service.mt102.SinglePayment;
import com.example.service.mt900.Mt900;
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

        testProcessMT900();

        return r;
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
}

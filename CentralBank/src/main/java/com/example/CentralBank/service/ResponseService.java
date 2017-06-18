package com.example.CentralBank.service;

import com.example.CentralBank.service.jaxws.ProcessMT900;
import com.example.CentralBank.service.jaxws.ProcessMT900Response;
import com.example.CentralBank.service.jaxws.ProcessMT910;
import com.example.CentralBank.service.jaxws.ProcessMT910Response;
import com.example.service.mt900.Mt900;
import com.example.service.mt910.Mt910;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

@Service
public class ResponseService extends WebServiceGatewaySupport{

    public void sendResponseMT900() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(ProcessMT900.class, ProcessMT900Response.class);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

        ProcessMT900 p = new ProcessMT900();
        Mt900 m = new Mt900();
        m.setMessageId("testiram soap za mt900");
        p.setArg0(m);

        String uri = "https://localhost:8080/ws/mt900";
        Object o = getWebServiceTemplate().marshalSendAndReceive(uri, p);
        ProcessMT900Response response = (ProcessMT900Response) o;
        System.out.println("**************************************");
        System.out.println(response.getReturn());
    }

    public void sendResponseMT910() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(ProcessMT910.class, ProcessMT910Response.class);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

        ProcessMT910 p = new ProcessMT910();
        Mt910 m = new Mt910();
        m.setMessageId("testiram soap za mt910");
        p.setArg0(m);

        String uri = "https://localhost:8080/ws/mt910";
        Object o = getWebServiceTemplate().marshalSendAndReceive(uri, p);
        ProcessMT910Response response = (ProcessMT910Response) o;
        System.out.println("**************************************");
        System.out.println(response.getReturn());
    }
}

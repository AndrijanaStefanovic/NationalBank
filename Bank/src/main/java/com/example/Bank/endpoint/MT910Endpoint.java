package com.example.Bank.endpoint;

import com.example.Bank.service.jaxws.ProcessMT910;
import com.example.Bank.service.jaxws.ProcessMT910Response;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class MT910Endpoint {

    private static final String NAMESPACE_URI = "http://service.Bank.example.com/";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "processMT910")
    @ResponsePayload
    public ProcessMT910Response processMT910(@RequestPayload ProcessMT910 mt910) {
        ProcessMT910Response response = new ProcessMT910Response();
        System.out.println(mt910.getArg0().getClass().getSimpleName());
        response.setReturn("test return mt910");
        return response;
    }
}

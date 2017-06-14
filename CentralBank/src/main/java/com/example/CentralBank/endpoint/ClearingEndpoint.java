package com.example.CentralBank.endpoint;

import com.example.CentralBank.service.jaxws.ProcessMT102;
import com.example.CentralBank.service.jaxws.ProcessMT102Response;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class ClearingEndpoint {

    private static final String NAMESPACE_URI = "http://service.CentralBank.example.com/";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "processMT102")
    @ResponsePayload
    public ProcessMT102Response processMT102(@RequestPayload ProcessMT102 mt102) {
        System.out.println(mt102.getArg0().getMessageId());
        ProcessMT102Response r = new ProcessMT102Response();
        r.setReturn("test return mt102");
        return r;
    }
}

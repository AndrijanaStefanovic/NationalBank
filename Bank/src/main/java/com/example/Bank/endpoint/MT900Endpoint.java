package com.example.Bank.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.example.Bank.service.ClearingService;
import com.example.Bank.service.jaxws.ProcessMT900;
import com.example.Bank.service.jaxws.ProcessMT900Response;
import com.example.service.mt900.Mt900;

@Endpoint
public class MT900Endpoint {

    private static final String NAMESPACE_URI = "http://service.Bank.example.com/";

    @Autowired
    private ClearingService clearingService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "processMT900")
    @ResponsePayload
    public ProcessMT900Response processMT900(@RequestPayload ProcessMT900 processMt900) {
        ProcessMT900Response response = new ProcessMT900Response();
        Mt900 mt900 = processMt900.getArg0();
        System.out.println("received mt900...");
        response.setReturn(clearingService.processMt900(mt900));
        return response;
    }
}

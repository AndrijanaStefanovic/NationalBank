package com.example.Bank.endpoint;

import com.example.Bank.service.jaxws.ProcessMT900;
import com.example.Bank.service.jaxws.ProcessMT900Response;
import com.example.service.bankstatement.BankStatement;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class MT900Endpoint {

    private static final String NAMESPACE_URI = "http://service.Bank.example.com/";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "processMT900")
    @ResponsePayload
    public ProcessMT900Response processMT900(@RequestPayload ProcessMT900 mt900) {
        ProcessMT900Response response = new ProcessMT900Response();
        System.out.println(mt900.getArg0().getClass().getSimpleName());
        response.setReturn("test return mt900");
        return response;
    }
}

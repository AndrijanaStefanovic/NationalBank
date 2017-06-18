package com.example.Bank.service;

import com.example.Bank.service.jaxws.ProcessMT102;
import com.example.Bank.service.jaxws.ProcessMT102Response;
import com.example.service.mt102.Mt102;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

@Service
public class ClearingClientServiceImpl extends WebServiceGatewaySupport implements ClearingClientService {

    @Override
    public String sendMt102(Mt102 mt102) {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(ProcessMT102.class, ProcessMT102Response.class);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

        ProcessMT102 processMt102 = new ProcessMT102();
        processMt102.setArg0(mt102);

        String uri = "http://localhost:8090/ws/mt102";
        Object o = getWebServiceTemplate().marshalSendAndReceive(uri, processMt102);

        ProcessMT102Response response = (ProcessMT102Response)o;
        return response.getReturn();
    }
}

package com.example.Bank.service;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.example.Bank.service.jaxws.ProcessMT103;
import com.example.Bank.service.jaxws.ProcessMT103Response;
import com.example.service.mt103.Mt103;

@Service
public class SOAPClientServiceImpl extends WebServiceGatewaySupport  implements SOAPClientService{

	@Override
	public String sendMt103(Mt103 mt103) {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(ProcessMT103.class, ProcessMT103Response.class);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

        ProcessMT103 processMt103 = new ProcessMT103();
        processMt103.setArg0(mt103);

        String uri = "https://localhost:8090/ws/mt103";
        Object o = getWebServiceTemplate().marshalSendAndReceive(uri, processMt103);
        
        ProcessMT103Response response = (ProcessMT103Response)o;
		return response.getReturn();
	}

}

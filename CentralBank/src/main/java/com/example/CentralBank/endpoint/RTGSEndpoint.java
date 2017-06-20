package com.example.CentralBank.endpoint;

import com.example.CentralBank.service.RTGSService;
import com.example.CentralBank.service.ResponseService;
import com.example.CentralBank.service.jaxws.*;
import com.example.service.mt103.Mt103;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class RTGSEndpoint extends WebServiceGatewaySupport {
	
	private static final String NAMESPACE_URI = "http://service.CentralBank.example.com/";

	@Autowired
	private RTGSService RTGSService;

	@Autowired
	private ResponseService responseService;
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "processMT103")
	@ResponsePayload
	public ProcessMT103Response processMT103(@RequestPayload ProcessMT103 mt103) {
		System.out.println(mt103.getArg0().getMessageId());
		ProcessMT103Response r = new ProcessMT103Response();
		r.setReturn("test return mt103");
		RTGSService.processMT103(mt103.getArg0());

//		forwardMT103();

//		responseService.sendResponseMT900();

//		responseService.sendResponseMT910();

		return r;
	}

	private void forwardMT103() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(ProcessMT103Normal.class, ProcessMT103ResponseNormal.class);
		setMarshaller(marshaller);
		setUnmarshaller(marshaller);

		ProcessMT103Normal p = new ProcessMT103Normal();
		Mt103 m = new Mt103();
		m.setMessageId("testiram forward soap-a cb na normalnu banku");
		p.setArg0(m);

		String uri = "https://localhost:8080/ws/mt103";
		Object o = getWebServiceTemplate().marshalSendAndReceive(uri, p);
		ProcessMT103ResponseNormal response = (ProcessMT103ResponseNormal)o;
		System.out.println("**************************************");
		System.out.println(response.getReturn());
	}
}

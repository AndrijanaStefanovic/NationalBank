package com.example.Bank.endpoint;

import com.example.Bank.service.RTGSService;
import com.example.Bank.service.jaxws.ProcessMT103;
import com.example.Bank.service.jaxws.ProcessMT103Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class RTGSEndpoint {
	
	private static final String NAMESPACE_URI = "http://service.Bank.example.com/";

	@Autowired
	RTGSService rtgsService;
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "processMT103")
	@ResponsePayload
	public ProcessMT103Response processMT103(@RequestPayload ProcessMT103 mt103) {
		System.out.println(mt103.getArg0().getMessageId());
		ProcessMT103Response r = new ProcessMT103Response();
		r.setReturn("test return mt103");
		rtgsService.processMT103(mt103.getArg0());

		System.out.println("Primio sam forwardovan mt103");

		return r;
	}
}

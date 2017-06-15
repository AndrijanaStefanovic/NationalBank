package com.example.CentralBank.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.example.CentralBank.service.RTGSService;
import com.example.CentralBank.service.jaxws.ProcessMT103;
import com.example.CentralBank.service.jaxws.ProcessMT103Response;

@Endpoint
public class RTGSEndpoint {
	
	private static final String NAMESPACE_URI = "http://service.CentralBank.example.com/";

	@Autowired
	private RTGSService RTGSService;
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "processMT103")
	@ResponsePayload
	public ProcessMT103Response processMT103(@RequestPayload ProcessMT103 mt103) {
		System.out.println(mt103.getArg0().getMessageId());
		ProcessMT103Response r = new ProcessMT103Response();
		r.setReturn("test return mt103");
		RTGSService.processMT103(mt103.getArg0());
		return r;
	}
}

package com.example.CentralBank;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import com.example.CentralBank.service.jaxws.ProcessMT103;
import com.example.CentralBank.service.jaxws.ProcessMT103Response;
import com.example.service.mt103.Mt103;

public class Client extends WebServiceGatewaySupport{
	
	static {
		DisableSSlLVerification.disableSslVerification();
	}
	
	public void testProcessMT103() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(ProcessMT103.class, ProcessMT103Response.class);
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

        ProcessMT103 p = new ProcessMT103();
		Mt103 m = new Mt103();
		m.setMessageId("testiram soap cb");
		p.setArg0(m);

        String uri = "https://localhost:8090/ws/mt103";
        Object o = getWebServiceTemplate().marshalSendAndReceive(uri, p);
        ProcessMT103Response response = (ProcessMT103Response)o;
        System.out.println("**************************************");
        System.out.println(response.getReturn());
    }
}

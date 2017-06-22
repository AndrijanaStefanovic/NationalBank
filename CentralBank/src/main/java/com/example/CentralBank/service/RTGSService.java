package com.example.CentralBank.service;

import com.example.service.mt103.Mt103;

public interface RTGSService {
	
	public String processMT103(Mt103 mt103);
	
	public String sendMT900(Mt103 mt103);
	
	public String sendMT910(Mt103 mt103);

	public void forwardMT103(Mt103 arg0);
	
}

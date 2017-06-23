package com.example.Bank.service;

import com.example.service.mt103.Mt103;
import com.example.service.mt900.Mt900;
import com.example.service.mt910.Mt910;

public interface RTGSService {

	public String processMT103(Mt103 mt103);
	
	public String processMT900(Mt900 mt900);

	public String processMT910(Mt910 mt910);
	
}

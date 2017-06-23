package com.example.CentralBank.service.certificates;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import model.CertificateModel;

@Service
public class CertificateServiceImpl implements CertificateService {

	@Override
	public String checkSerialNumber(String serialNum) {
		Integer serialNumber = Integer.parseInt(serialNum);
		HashMap<String, CertificateModel> certificateModels = new HashMap<String, CertificateModel>();
		CertificateModel helperCertificate = new CertificateModel();
		boolean found = false;
		String result = "";
		
		try {
			certificateModels = helperCertificate.load();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		for (CertificateModel cm : certificateModels.values()) {
			if(cm.getSerialNumber() == serialNumber) {
				found = true;
				result = "Certificate is NOT withdrawn!";
			}
		}
		if(!found) 
			result = "Certificate is withdrawn!";
		
		return result;
	}

}

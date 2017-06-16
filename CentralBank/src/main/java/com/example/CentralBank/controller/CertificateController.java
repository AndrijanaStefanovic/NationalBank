package com.example.CentralBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.CentralBank.service.certificates.CertificateService;


@RestController
public class CertificateController {
	
	@Autowired
	private CertificateService certificateService;
	
	//test for serial number: 6892989
	@RequestMapping(
			value = "/certificate/checkSerialNumber/{serialNumber}",
			method = RequestMethod.GET,
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	public String checkSerialNumber(@PathVariable("serialNumber") String serialNumber) {
		System.out.println("Res: " + certificateService.checkSerialNumber(serialNumber));
		return certificateService.checkSerialNumber(serialNumber);
	}

}

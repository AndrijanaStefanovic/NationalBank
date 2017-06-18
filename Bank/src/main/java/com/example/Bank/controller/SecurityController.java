package com.example.Bank.controller;



import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Bank.service.SecurityService;

@RestController
public class SecurityController {

	@Autowired
	private SecurityService securityService;
	
	@RequestMapping(value = "/bank/receiveKey",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response receiveKey(@RequestBody String key) {
		securityService.decryptSessionKey(Base64.getDecoder().decode(key));
		return Response.status(200).entity("ok").build();
	}
}

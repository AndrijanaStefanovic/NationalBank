package com.example.Bank.service;

public interface SecurityService {

	public void decryptSessionKey(byte[] key);
	
	public void validateWithSchema(Object object);
	
	public boolean validateSignature(Object object);
}

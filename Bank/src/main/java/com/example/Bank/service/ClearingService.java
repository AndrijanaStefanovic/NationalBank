package com.example.Bank.service;

import com.example.service.mt102.Mt102;
import com.example.service.mt900.Mt900;
import com.example.service.mt910.Mt910;

public interface ClearingService {

    public String processMT102(Mt102 mt102);
    
	/**
	 * Funkcija koja prima poruku o zaduzenju i zaduzuje racun klijenta
	 * */
	public String processMt900(Mt900 mt900);
	
	
	/**
	 * Funkcija koja prima poruku o odobrenju i uplacuje sredstva na racun klijenta
	 * */
	public String processMt910(Mt910 mt910);
}

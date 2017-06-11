package com.example.Bank.service;

import com.example.service.paymentorder.PaymentOrder;

public interface PaymentService {

	/**
	 * Funkcija zaduzuje racun posiljaoca naloga za placanje, kreira analitiku izvoda za ovo placanje i azurira dnevni
	 * balans racuna, odnosno kreira ga ukoliko je ovo prvi poslati nalog za taj racun tog dana.
	 * */
	public String createAccountAnalytics(PaymentOrder paymentOrder);
}

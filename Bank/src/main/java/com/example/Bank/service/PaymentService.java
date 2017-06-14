package com.example.Bank.service;

import com.example.service.mt102.Mt102;
import com.example.service.mt103.Mt103;
import com.example.service.paymentorder.PaymentOrder;

public interface PaymentService {

	/**
	 * Funkcija zaduzuje racun posiljaoca naloga za placanje, kreira analitiku izvoda za ovo placanje i azurira dnevni
	 * balans racuna, odnosno kreira ga ukoliko je ovo prvi poslati nalog za taj racun tog dana.
	 * */
	public String createDebtorAccountAnalytics(PaymentOrder paymentOrder);
	
	/**
	 * Funkcija koja proverava da li se zadati broj racuna nalazi u banci
	 * */
	public boolean checkIfClient(String accountNumber);
	
	/**
	 * Funkcija koja uplacuje sredstva specificirana nalogom za placanje na odgovarajuci racun, za slucaj da je creditor
	 * u istoj banci kao i debtor
	 * */
	public String createCreditorAccountAnalytics(PaymentOrder paymentOrder);
	
	
	public Mt103 createMT103(PaymentOrder paymentOrder);

	public Mt102 createMT102(PaymentOrder paymentOrder);
}

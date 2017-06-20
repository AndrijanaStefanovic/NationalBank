package com.example.Bank.service;

import com.example.Bank.model.SinglePaymentModel;
import com.example.service.mt103.Mt103;
import com.example.service.mt900.Mt900;
import com.example.service.paymentorder.PaymentOrder;

public interface PaymentService {

	/**
	 * Funkcija zaduzuje racun posiljaoca naloga za placanje, kreira analitiku izvoda za ovo placanje i azurira dnevni
	 * balans racuna, odnosno kreira ga ukoliko je ovo prvi poslati nalog za taj racun tog dana.
	 * */
	public String createDebtorAccountAnalytics(PaymentOrder paymentOrder, boolean isClearing);
	
	/**
	 * Funkcija koja proverava da li se zadati broj racuna nalazi u banci
	 * */
	public boolean checkIfClient(String accountNumber);
	
	/**
	 * Funkcija koja uplacuje sredstva specificirana nalogom za placanje na odgovarajuci racun, za slucaj da je creditor
	 * u istoj banci kao i debtor
	 * */
	public String createCreditorAccountAnalytics(PaymentOrder paymentOrder);
	
	/**
	 * Kreira Mt103 nalog na osnovu primljenog naloga za placanje
	 * */
	public Mt103 createMT103(PaymentOrder paymentOrder);

	/**
	 * Kreira novi Mt102 model, dodaje mu jednu stavku i cuva ga u bazu
	 * */
	public String createMT102Model(PaymentOrder paymentOrder, SinglePaymentModel singlePaymentModel);

	/**
	 * Kreira deo mt102 na osnovu jednog naloga. Ako zaglavlje mt102 za tu banku koje nije poslato
	 * ne postoji, poziva funkciju koja ga kreira
	 * */
	public String createSinglePaymentForMt012(PaymentOrder paymentOrder);
	
	/**
	 * Funkcija koja ce rezervisati sredstva na osnovu naloga za placanje na pocetku Clearinga
	 * */
	public String reserveFunds(String accountNumber, double amount);
	
	/**
	 * Funkcija koja za broj racuna klijenta vraca SWIFT kod njegove banke
	 * */
	public String getBanksSwift(String clientsAccountNumber);
	
	
}

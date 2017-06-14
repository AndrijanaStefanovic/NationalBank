package com.example.Bank.service;

import java.util.*;

import com.example.service.mt102.Mt102;
import com.example.service.mt102.SinglePayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Bank.model.Account;
import com.example.Bank.model.AccountAnalytics;
import com.example.Bank.model.Bank;
import com.example.Bank.model.DailyAccountBalance;
import com.example.Bank.repository.AccountAnalyticsRepository;
import com.example.Bank.repository.AccountRepository;
import com.example.Bank.repository.BankRepository;
import com.example.Bank.repository.DailyAccountBalanceRepository;
import com.example.service.mt103.Mt103;
import com.example.service.mt103.TBankData;
import com.example.service.paymentorder.PaymentOrder;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountAnalyticsRepository accountAnalyticsRepository;
	
	@Autowired
	private DailyAccountBalanceRepository dailyAccountBalanceRepository;
	
	@Autowired
	private BankRepository bankRepository;
	
	@Override
	public String createDebtorAccountAnalytics(PaymentOrder paymentOrder) {
		Date dateOfPayment = paymentOrder.getDateOfPayment().toGregorianCalendar().getTime();
		Date dateOfValue = paymentOrder.getDateOfValue().toGregorianCalendar().getTime();
		double amount = Double.parseDouble(paymentOrder.getAmount().toString());
		List<Account> retList = accountRepository.findByAccountNumber(paymentOrder.getDebtor().getAccountNumber());
		if(retList.isEmpty()){
			return "AccountNotFound";
		}
		Account debtorsAccount = retList.get(0);
		Bank debtorsBank = debtorsAccount.getBank();
		
		String bankCode = paymentOrder.getCreditor().getAccountNumber().substring(0, 3);
		List<Bank> banks = bankRepository.findByCode(Integer.parseInt(bankCode));
		if(banks.isEmpty()){
			return "BankNotFound";
		}
		Bank creditorsBank = banks.get(0);
		AccountAnalytics debtorsAccountAnalytics = new AccountAnalytics(debtorsBank.getSWIFTcode(), 
				debtorsBank.getAccountNumber(),
				creditorsBank.getSWIFTcode(), 
				creditorsBank.getAccountNumber(),
				paymentOrder.getDebtor().getInfo(), 
				paymentOrder.getPaymentPurpose(), 
				paymentOrder.getCreditor().getInfo(),
				dateOfPayment, 
				dateOfValue, 
				paymentOrder.getDebtor().getAccountNumber(), 
				paymentOrder.getDebtor().getModel(), 
				paymentOrder.getDebtor().getReferenceNumber(), 
				paymentOrder.getCreditor().getAccountNumber(), 
				paymentOrder.getCreditor().getModel(), 
				paymentOrder.getCreditor().getReferenceNumber(),
				amount, 
				paymentOrder.getCurrency(),
				false);
		Date poDate = paymentOrder.getDateOfPayment().toGregorianCalendar().getTime();
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(poDate);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        poDate = calendar.getTime();
        boolean dabExists = false;
		for (DailyAccountBalance dab : debtorsAccount.getDailyAccountBalances()) {
			Date dabDate = dab.getDate();
			calendar.setTime(poDate);
	        calendar.set(Calendar.MILLISECOND, 0);
	        calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.HOUR, 0);
	        dabDate = calendar.getTime();
			
	        if(dabDate.equals(poDate)){
	        	dab.getAccountAnalytics().add(debtorsAccountAnalytics);
	        	debtorsAccountAnalytics.setDailyAccountBalance(dab);
	        	dab.setNumberOfWithdrawals(dab.getNumberOfWithdrawals() + 1);
	        	dab.setNewBalance(debtorsAccount.getBalance() - amount);
	        	dailyAccountBalanceRepository.save(dab);
	        	dabExists = true;
	        	break;
	        }
		}
		if(!dabExists) {
			DailyAccountBalance newDab = new DailyAccountBalance(poDate, debtorsAccount.getBalance(),
					1, debtorsAccount.getBalance() - amount, 0, debtorsAccount);
			DailyAccountBalance newDabDB = dailyAccountBalanceRepository.save(newDab);
			DailyAccountBalance newDab2 = dailyAccountBalanceRepository.findOne(newDabDB.getId());
			debtorsAccountAnalytics.setDailyAccountBalance(newDab2);
			newDab2.getAccountAnalytics().add(debtorsAccountAnalytics);
			debtorsAccount.getDailyAccountBalances().add(newDab2);
			dailyAccountBalanceRepository.save(newDabDB);
		}
		
		debtorsAccount.setBalance(debtorsAccount.getBalance() - amount);
		accountRepository.save(debtorsAccount);
		accountAnalyticsRepository.save(debtorsAccountAnalytics);
	
		return "OK";
	}

	@Override
	public boolean checkIfClient(String accountNumber) {
		List<Account> accounts = accountRepository.findByAccountNumber(accountNumber);
		return !accounts.isEmpty();
	}

	@Override
	public String createCreditorAccountAnalytics(PaymentOrder paymentOrder) {
		Date dateOfPayment = paymentOrder.getDateOfPayment().toGregorianCalendar().getTime();
		Date dateOfValue = paymentOrder.getDateOfValue().toGregorianCalendar().getTime();
		double amount = Double.parseDouble(paymentOrder.getAmount().toString());
		List<Account> retList = accountRepository.findByAccountNumber(paymentOrder.getCreditor().getAccountNumber());
		if(retList.isEmpty()){
			return "AccountNotFound";
		}
		Account creditorsAccount = retList.get(0);
		Bank bank = creditorsAccount.getBank();
		
		AccountAnalytics creditorsAccountAnalytics = new AccountAnalytics(bank.getSWIFTcode(), 
				bank.getAccountNumber(), 
				bank.getSWIFTcode(), 
				bank.getAccountNumber(), 
				paymentOrder.getDebtor().getInfo(), 
				paymentOrder.getPaymentPurpose(), 
				paymentOrder.getCreditor().getInfo(),
				dateOfPayment, 
				dateOfValue, 
				paymentOrder.getDebtor().getAccountNumber(), 
				paymentOrder.getDebtor().getModel(), 
				paymentOrder.getDebtor().getReferenceNumber(), 
				paymentOrder.getCreditor().getAccountNumber(), 
				paymentOrder.getCreditor().getModel(), 
				paymentOrder.getCreditor().getReferenceNumber(),
				amount, 
				paymentOrder.getCurrency(),
				true); //razlika u ovom booleanu samo
		
		Date poDate = paymentOrder.getDateOfPayment().toGregorianCalendar().getTime();
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(poDate);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        poDate = calendar.getTime();
        boolean dabExists = false;
		for (DailyAccountBalance dab : creditorsAccount.getDailyAccountBalances()) {
			Date dabDate = dab.getDate();
			calendar.setTime(poDate);
	        calendar.set(Calendar.MILLISECOND, 0);
	        calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.HOUR, 0);
	        dabDate = calendar.getTime();
			
	        if(dabDate.equals(poDate)){
	        	dab.getAccountAnalytics().add(creditorsAccountAnalytics);
	        	creditorsAccountAnalytics.setDailyAccountBalance(dab);
	        	dab.setNumberOfDeposits(dab.getNumberOfDeposits() + 1);
	        	dab.setNewBalance(creditorsAccount.getBalance() + amount);
	        	dailyAccountBalanceRepository.save(dab);
	        	dabExists = true;
	        	break;
	        }
		}
		if(!dabExists) {
			DailyAccountBalance newDab = new DailyAccountBalance(poDate, creditorsAccount.getBalance(),
					0, creditorsAccount.getBalance() + amount, 1, creditorsAccount);
			DailyAccountBalance newDabDB = dailyAccountBalanceRepository.save(newDab);
			DailyAccountBalance newDab2 = dailyAccountBalanceRepository.findOne(newDabDB.getId());
			creditorsAccountAnalytics.setDailyAccountBalance(newDab2);
			newDab2.getAccountAnalytics().add(creditorsAccountAnalytics);
			creditorsAccount.getDailyAccountBalances().add(newDab2);
			dailyAccountBalanceRepository.save(newDabDB);
		}
		
		creditorsAccount.setBalance(creditorsAccount.getBalance() + amount);
		accountRepository.save(creditorsAccount);
		accountAnalyticsRepository.save(creditorsAccountAnalytics);
		
		return "OK";
	}

	@Override
	public Mt103 createMT103(PaymentOrder paymentOrder) {
		Mt103 mt103 = new Mt103();
		mt103.setMessageId("?");
		mt103.setDateOfPayment(paymentOrder.getDateOfPayment());
		mt103.setDateOfValue(paymentOrder.getDateOfValue());
		
		//debtor + debtors bank
		com.example.service.mt103.TCompanyData debtorData = new com.example.service.mt103.TCompanyData();
		debtorData.setInfo(paymentOrder.getDebtor().getInfo());
		debtorData.setAccountNumber(paymentOrder.getDebtor().getAccountNumber());
		debtorData.setModel(paymentOrder.getDebtor().getModel());
		debtorData.setReferenceNumber(paymentOrder.getDebtor().getReferenceNumber());
		mt103.setDebtor(debtorData);
		
		List<Account> retList = accountRepository.findByAccountNumber(paymentOrder.getDebtor().getAccountNumber());
		if(retList.isEmpty()){
			return null;
		}
		Account debtorsAccount = retList.get(0);
		Bank debtorsBank = debtorsAccount.getBank();
		
		TBankData debtorsBankData = new TBankData();
		debtorsBankData.setAccountNumber(debtorsBank.getAccountNumber());
		debtorsBankData.setSWIFT(debtorsBank.getSWIFTcode());
		mt103.setDebtorsBank(debtorsBankData);
		
		//creditor + creditors bank
		com.example.service.mt103.TCompanyData creditorData = new com.example.service.mt103.TCompanyData();
		creditorData.setInfo(paymentOrder.getCreditor().getInfo());
		creditorData.setAccountNumber(paymentOrder.getCreditor().getAccountNumber());
		creditorData.setModel(paymentOrder.getCreditor().getModel());
		creditorData.setReferenceNumber(paymentOrder.getCreditor().getReferenceNumber());
		mt103.setCreditor(creditorData);
		String bankCode = paymentOrder.getCreditor().getAccountNumber().substring(0, 3);
		List<Bank> banks = bankRepository.findByCode(Integer.parseInt(bankCode));
		if(banks.isEmpty()){
			return null;
		}
		Bank creditorsBank = banks.get(0);
		TBankData creditorsBankData = new TBankData();
		creditorsBankData.setAccountNumber(creditorsBank.getAccountNumber());
		creditorsBankData.setSWIFT(creditorsBank.getSWIFTcode());
		mt103.setCreditorsBank(creditorsBankData);

		
		mt103.setCurrency(paymentOrder.getCurrency());
		mt103.setPaymentPurpose(paymentOrder.getPaymentPurpose());
		mt103.setTotal(paymentOrder.getAmount());
		return mt103;
	}

	@Override
	public Mt102 createMT102(PaymentOrder paymentOrder) {
		Mt102 mt102 = new Mt102();
		//message id
		mt102.setMessageId(UUID.randomUUID().toString());

		//creditors swift and accountNumber
		TBankData creditorsBankData = new TBankData();
		mt102.setCreditorSwift(creditorsBankData.getSWIFT());
		mt102.setCreditorAccountNumber(paymentOrder.getCreditor().getAccountNumber());

		//debtors swift and accountNumber
		TBankData debtorsBankData = new TBankData();
		mt102.setDebtorSwift(debtorsBankData.getSWIFT());
		mt102.setDebtorAccountNumber(paymentOrder.getDebtor().getAccountNumber());

		//dates
		mt102.setDateOfPayment(paymentOrder.getDateOfPayment());
		mt102.setDateOfValue(paymentOrder.getDateOfValue());

		//currency
		mt102.setCurrency(paymentOrder.getCurrency());

		//total
		mt102.setTotal(paymentOrder.getAmount());

		//sequence single payments
		List<SinglePayment> currentSinglePayments = mt102.getSinglePayment();
		for (int i = 0; i < 5; i++) {
			currentSinglePayments.add(generateSinglePayment(paymentOrder ,i));
		}

		SinglePayment singlePayment = new SinglePayment();

		return mt102;
	}

	private SinglePayment generateSinglePayment(PaymentOrder paymentOrder, int i) {
		SinglePayment singlePayment = new SinglePayment();

		//paymentID
		singlePayment.setPaymentId(i+"ID"+UUID.randomUUID().toString());

		//purpose
		singlePayment.setPaymentPurpose(paymentOrder.getPaymentPurpose());

		//creditor
		com.example.service.mt102.TCompanyData creditorData = new com.example.service.mt102.TCompanyData();
		creditorData.setInfo(paymentOrder.getCreditor().getInfo());
		creditorData.setAccountNumber(paymentOrder.getCreditor().getAccountNumber());
		creditorData.setModel(paymentOrder.getCreditor().getModel());
		creditorData.setReferenceNumber(paymentOrder.getCreditor().getReferenceNumber());
		singlePayment.setCreditor(creditorData);

		//debtor
		com.example.service.mt102.TCompanyData debtorData = new com.example.service.mt102.TCompanyData();
		debtorData.setInfo(paymentOrder.getDebtor().getInfo());
		debtorData.setAccountNumber(paymentOrder.getDebtor().getAccountNumber());
		debtorData.setModel(paymentOrder.getDebtor().getModel());
		debtorData.setReferenceNumber(paymentOrder.getDebtor().getReferenceNumber());
		singlePayment.setCreditor(debtorData);

		//dateOfOrder
		singlePayment.setDateOfOrder(paymentOrder.getDateOfPayment());

		//totalPayment
		singlePayment.setTotal(paymentOrder.getAmount());

		//currency
		singlePayment.setCurrency(paymentOrder.getCurrency());

		return  singlePayment;
	}

}

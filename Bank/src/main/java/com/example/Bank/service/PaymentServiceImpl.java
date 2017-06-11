package com.example.Bank.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.example.service.mt103.TCompanyData;

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
		TCompanyData debtorData = new TCompanyData();
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
		TCompanyData creditorData = new TCompanyData();
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

}

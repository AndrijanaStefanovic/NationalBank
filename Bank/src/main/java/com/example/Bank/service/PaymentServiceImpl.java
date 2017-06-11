package com.example.Bank.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Bank.model.Account;
import com.example.Bank.model.AccountAnalytics;
import com.example.Bank.model.DailyAccountBalance;
import com.example.Bank.repository.AccountAnalyticsRepository;
import com.example.Bank.repository.AccountRepository;
import com.example.Bank.repository.DailyAccountBalanceRepository;
import com.example.service.paymentorder.PaymentOrder;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountAnalyticsRepository accountAnalyticsRepository;
	
	@Autowired
	private DailyAccountBalanceRepository dailyAccountBalanceRepository;
	
	@Override
	public String createAccountAnalytics(PaymentOrder paymentOrder) {
		Date dateOfPayment = paymentOrder.getDateOfPayment().toGregorianCalendar().getTime();
		Date dateOfValue = paymentOrder.getDateOfValue().toGregorianCalendar().getTime();
		double amount = Double.parseDouble(paymentOrder.getAmount().toString());
		List<Account> retList = accountRepository.findByAccountNumber(paymentOrder.getDebtor().getAccountNumber());
		if(retList.isEmpty()){
			return null;
		}
		Account debtorsAccount = retList.get(0);
		
		AccountAnalytics debtorsAccountAnalytics = new AccountAnalytics("mySwift", "myAccount", "otherBanksSwift", "otherBanksAccount", 
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

}

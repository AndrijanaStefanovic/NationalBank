package com.example.Bank.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.example.Bank.model.Account;
import com.example.Bank.model.AccountAnalytics;
import com.example.Bank.model.DailyAccountBalance;
import com.example.Bank.model.Mt102Model;
import com.example.Bank.model.SinglePaymentModel;
import com.example.Bank.repository.AccountAnalyticsRepository;
import com.example.Bank.repository.AccountRepository;
import com.example.Bank.repository.DailyAccountBalanceRepository;
import com.example.Bank.repository.Mt102Repository;
import com.example.service.mt102.Mt102;
import com.example.service.mt900.Mt900;
import com.example.service.mt910.Mt910;

@Service
public class ClearingServiceImpl extends WebServiceGatewaySupport implements ClearingService{

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountAnalyticsRepository accountAnalyticsRepository;
	
	@Autowired
	private DailyAccountBalanceRepository dailyAccountBalanceRepository;
	
	@Autowired
	private Mt102Repository mt102Repository;

    @Override
    public String processMT102(Mt102 mt102) {

        return "OK";
    }
    
    @Override
	public String processMt900(Mt900 mt900) {
		System.out.println("processing mt900...");
		Mt102Model mt102 = mt102Repository.findOne(Long.parseLong(mt900.getOrderMessageId()));
		
		for (SinglePaymentModel spm : mt102.getSinglePaymentModels()) {

			List<Account> accounts = accountRepository.findByAccountNumber(spm.getDebtorAccountNumber());
			if (accounts.isEmpty()) {
				return "accountNotFound";
			}
			Account debtorsAccount = accounts.get(0);
			debtorsAccount.setBalance(debtorsAccount.getBalance() - spm.getTotal());
			debtorsAccount.setReservedFunds(debtorsAccount.getReservedFunds() - spm.getTotal());
			System.out.println("subtracted amount from debtors reserved funds...");

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(spm.getDateOfOrder());
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.HOUR, 0);
			Date dateOfOrder = calendar.getTime();

			for (DailyAccountBalance dab : debtorsAccount.getDailyAccountBalances()) {
				System.out.println("searching through dabs....");
				calendar.setTime(dab.getDate());
				calendar.set(Calendar.MILLISECOND, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.HOUR, 0);
				Date dabDate = calendar.getTime();

				if (dateOfOrder.equals(dabDate)) {
					System.out.println("found dab with adequate date...");
					for (AccountAnalytics aa : dab.getAccountAnalytics()) {
						if (aa.getDebtorsAccountNumber().equals(spm.getDebtorAccountNumber())
								&& aa.getReservedFunds() == spm.getTotal()) {
							System.out.println("found account analytics...");
							aa.setReservedFunds(0);
							aa.setAmount(spm.getTotal());
							dab.setPreviousBalance(dab.getNewBalance());
							dab.setNewBalance(dab.getNewBalance() - spm.getTotal());
							dailyAccountBalanceRepository.save(dab);
							accountAnalyticsRepository.save(aa);
							break;
						}
					}
					break;
				}
			}
			accountRepository.save(debtorsAccount);
		}
		return "OK";
	}

	@Override
	public String processMt910(Mt910 mt910) {
		System.out.println("processing mt910...");
		Mt102Model mt102 = mt102Repository.findOne(Long.parseLong(mt910.getOrderMessageId()));
		
		for (SinglePaymentModel spm : mt102.getSinglePaymentModels()) {

			List<Account> accounts = accountRepository.findByAccountNumber(spm.getCreditorAccountNumber());
			if (accounts.isEmpty()) {
				return "accountNotFound";
			}
			Account creditorsAccount = accounts.get(0);
			creditorsAccount.setBalance(creditorsAccount.getBalance() + spm.getTotal());
			System.out.println("added amount to creditors balance...");

			AccountAnalytics accountAnalytics = new AccountAnalytics(mt102.getDebtorSwift(),
					mt102.getDebtorAccountNumber(),
					mt102.getCreditorSwift(), 
					mt102.getCreditorAccountNumber(),
					spm.getDebtorInfo(), 
					spm.getPaymentPurpose(),
					spm.getCreditorInfo(), 
					mt102.getDateOfPayment(),
					mt102.getDateOfValue(), 
					spm.getDebtorAccountNumber(),
					spm.getDebtorModel(), 
					spm.getDebtorReferenceNumber(),
					spm.getCreditorAccountNumber(), 
					spm.getCreditorModel(), 
					spm.getCreditorReferenceNumber(),
					spm.getTotal(), 
					0.0, 
					spm.getCurrency(), 
					true);
			
			

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.HOUR, 0);
			Date today = calendar.getTime();
			
			boolean foundDab = false;
			for (DailyAccountBalance dab : creditorsAccount.getDailyAccountBalances()) {
				System.out.println("searching through dabs....");
				calendar.setTime(dab.getDate());
				calendar.set(Calendar.MILLISECOND, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.HOUR, 0);
				Date dabDate = calendar.getTime();

				if (today.equals(dabDate)) {
					System.out.println("found dab with adequate date...");
					dab.getAccountAnalytics().add(accountAnalytics);
					dab.setPreviousBalance(dab.getNewBalance());
					dab.setNewBalance(dab.getNewBalance() + spm.getTotal());
					dab.setNumberOfDeposits(dab.getNumberOfDeposits()+1);
					dailyAccountBalanceRepository.save(dab);
					foundDab = true;
					break;
				}
			}
			if(!foundDab){
				DailyAccountBalance newDab = new DailyAccountBalance(today,
						creditorsAccount.getBalance(), 
						0, 
						creditorsAccount.getBalance()+spm.getTotal(), 
						1, 
						creditorsAccount);
				dailyAccountBalanceRepository.save(newDab);
				//dodati analitiku
			}
			
			//accountRepository.save(debtorsAccount);
		}
		return "OK";
	}
}

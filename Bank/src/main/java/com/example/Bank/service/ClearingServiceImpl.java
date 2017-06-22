package com.example.Bank.service;

import java.util.ArrayList;
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
import com.example.Bank.repository.BankRepository;
import com.example.Bank.repository.DailyAccountBalanceRepository;
import com.example.Bank.repository.Mt102Repository;
import com.example.Bank.repository.SinglePaymentRepository;
import com.example.Bank.model.Bank;
import com.example.service.mt102.Mt102;
import com.example.service.mt102.SinglePayment;
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
	
	@Autowired
	private BankRepository bankRepository;
	
	@Autowired
	private SinglePaymentRepository singlePaymentRepository;

    @Override
    public String processMT102(Mt102 mt102) {
    	System.out.println("processing mt102 with uuid:"+mt102.getMessageId());
    	List<Bank> crb = bankRepository.findBySwiftCode(mt102.getCreditorSwift());
        if(crb.isEmpty()){
            return "creditorsBankSWIFTError";
        }

        List<Bank> dbb = bankRepository.findBySwiftCode(mt102.getDebtorSwift());
        if(dbb.isEmpty()){
            return "debtorsBankSWIFTError";
        }
        Mt102Model mt102Model = new Mt102Model();
        mt102Model.setCreditorAccountNumber(mt102.getCreditorAccountNumber());
        mt102Model.setSent(true);
        mt102Model.setCreditorSwift(mt102.getCreditorSwift());
        mt102Model.setDebtorAccountNumber(mt102.getDebtorAccountNumber());
        mt102Model.setDebtorSwift(mt102.getDebtorSwift());
        mt102Model.setCurrency(mt102.getCurrency());
        mt102Model.setDateOfValue(mt102.getDateOfValue().toGregorianCalendar().getTime());
        mt102Model.setDateOfPayment(mt102.getDateOfPayment().toGregorianCalendar().getTime());
        mt102Model.setTotal(mt102.getTotal().doubleValue());
        mt102Model.setMessageId(mt102.getMessageId());
        mt102Repository.save(mt102Model);

        ArrayList<SinglePaymentModel> spmList = new ArrayList<SinglePaymentModel>();
        for(SinglePayment sp : mt102.getSinglePayment()){
        	SinglePaymentModel spm = new SinglePaymentModel();
        	spm.setCreditorAccountNumber(sp.getCreditor().getAccountNumber());
        	spm.setCreditorInfo(sp.getCreditor().getInfo());
        	spm.setCreditorModel(sp.getCreditor().getModel());
        	spm.setCreditorReferenceNumber(sp.getCreditor().getReferenceNumber());
        	spm.setCurrency(sp.getCurrency());
        	spm.setDateOfOrder(sp.getDateOfOrder().toGregorianCalendar().getTime());
        	spm.setDebtorAccountNumber(sp.getDebtor().getAccountNumber());
        	spm.setDebtorInfo(sp.getDebtor().getInfo());
        	spm.setDebtorModel(sp.getDebtor().getModel());
        	spm.setDebtorReferenceNumber(sp.getDebtor().getReferenceNumber());
        	spm.setMt102(mt102Model);
        	spm.setPaymentPurpose(sp.getPaymentPurpose());
        	spm.setTotal(sp.getTotal().doubleValue());
        	SinglePaymentModel spmDB = singlePaymentRepository.save(spm);
        	spmList.add(spmDB);
        	
        }
        
        mt102Model.setSinglePaymentModels(spmList);
        mt102Repository.save(mt102Model);
        System.out.println("saved mt102....");
        return "OK";
    }
    
    @Override
	public String processMt900(Mt900 mt900) {
		System.out.println("processing mt900...");
		List<Mt102Model> mt102List = mt102Repository.findByMessageId(mt900.getMessageId());
		if(mt102List.isEmpty()){
			return "mt102NotFound";
		}
		Mt102Model mt102 = mt102List.get(0);
		System.out.println("found mt102...");
		for (SinglePaymentModel spm : mt102.getSinglePaymentModels()) {
			System.out.println("single payment processing...");
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
							//dab.setPreviousBalance(dab.getNewBalance());
							//dab.setNewBalance(dab.getNewBalance() - spm.getTotal());
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
		System.out.println("processing mt910 with uuid:"+mt910.getMessageId());
		List<Mt102Model> mt102List = mt102Repository.findByMessageId(mt910.getMessageId());
		if(mt102List.isEmpty()){
			System.out.println("mt102 not found....");
			return "mt102NotFound";
		}
		System.out.println("mt102 found - processing....");
		Mt102Model mt102 = mt102List.get(0);
		
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
			accountAnalyticsRepository.save(accountAnalytics);
			
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
					System.out.println("adding to existing dab...");
					dab.getAccountAnalytics().add(accountAnalytics);
					dab.setPreviousBalance(dab.getNewBalance());
					dab.setNewBalance(dab.getNewBalance() + spm.getTotal());
					dab.setNumberOfDeposits(dab.getNumberOfDeposits()+1);
					dailyAccountBalanceRepository.save(dab);
					accountAnalytics.setDailyAccountBalance(dab);
					foundDab = true;
					break;
				}
			}
			if(!foundDab){
				System.out.println("creating new dab........");
				DailyAccountBalance newDab = new DailyAccountBalance(today,
						creditorsAccount.getBalance(), 
						0, 
						creditorsAccount.getBalance()+spm.getTotal(), 
						1, 
						creditorsAccount);
				ArrayList<AccountAnalytics> accountAnalyticsList = new ArrayList<AccountAnalytics>();
				accountAnalyticsList.add(accountAnalytics);
				newDab.setAccountAnalytics(accountAnalyticsList);
				DailyAccountBalance newDabDB = dailyAccountBalanceRepository.save(newDab);
				accountAnalytics.setDailyAccountBalance(newDabDB);
			}
			accountRepository.save(creditorsAccount);
			accountAnalyticsRepository.save(accountAnalytics);
		}
		return "OK";
	}
}

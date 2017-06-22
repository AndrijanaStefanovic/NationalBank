package com.example.Bank.service;

import com.example.Bank.model.Account;
import com.example.Bank.model.AccountAnalytics;
import com.example.Bank.model.DailyAccountBalance;
import com.example.Bank.model.Mt103Model;
import com.example.Bank.repository.AccountAnalyticsRepository;
import com.example.Bank.repository.AccountRepository;
import com.example.Bank.repository.BankRepository;
import com.example.Bank.repository.DailyAccountBalanceRepository;
import com.example.Bank.repository.Mt103ModelRepository;
import com.example.service.mt103.Mt103;
import com.example.service.mt900.Mt900;
import com.example.service.mt910.Mt910;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RTGSServiceImpl implements RTGSService {

	@Autowired
	private BankRepository bankRepository;

	@Autowired
	private Mt103ModelRepository mt103ModelRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountAnalyticsRepository accountAnalyticsRepository;

	@Autowired
	private DailyAccountBalanceRepository dailyAccountBalanceRepository;

	@Override
	public String processMT103(Mt103 mt103) {
		Mt103Model mt103Model = new Mt103Model(mt103);
		mt103ModelRepository.save(mt103Model);
		return "OK";
	}

	@Override
	public String processMT900(Mt900 mt900) {
		System.out.println("processing mt900...");
		List<Mt103Model> mt103List = mt103ModelRepository.findByMessageId(mt900.getMessageId());
		if (mt103List.isEmpty()) {
			return "mt102NotFound";
		}
		Mt103Model mt103 = mt103List.get(0);
		System.out.println("found mt102...");

		List<Account> accounts = accountRepository.findByAccountNumber(mt103.getDebtorAccountNumber());
		if (accounts.isEmpty()) {
			return "accountNotFound";
		}
		Account debtorsAccount = accounts.get(0);
		debtorsAccount.setBalance(debtorsAccount.getBalance() - mt103.getTotal());
		debtorsAccount.setReservedFunds(debtorsAccount.getReservedFunds() - mt103.getTotal());
		System.out.println("subtracted amount from debtors reserved funds...");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mt103.getDateOfPayment());
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, 0);
		Date dateOfOrder = calendar.getTime();

		System.out.println("date of order: "+dateOfOrder);
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
					System.out.println("mt 103 total:"+mt103.getTotal());
					if (aa.getDebtorsAccountNumber().equals(mt103.getDebtorAccountNumber())
							&& aa.getReservedFunds() == mt103.getTotal()) {
						System.out.println("found account analytics...");
						aa.setReservedFunds(0);
						aa.setAmount(mt103.getTotal());
						//dab.setPreviousBalance(dab.getNewBalance());
						//dab.setNewBalance(dab.getNewBalance() - mt103.getTotal());
						dailyAccountBalanceRepository.save(dab);
						accountAnalyticsRepository.save(aa);
						break;
					}
				}
				break;
			}

		}
		accountRepository.save(debtorsAccount);

		return "OK";
	}

	@Override
	public String processMT910(Mt910 mt910) {
		System.out.println("processing mt910 with uuid:" + mt910.getMessageId());
		List<Mt103Model> mt103List = mt103ModelRepository.findByMessageId(mt910.getMessageId());
		if (mt103List.isEmpty()) {
			System.out.println("mt103 not found....");
			return "mt103NotFound";
		}
		System.out.println("mt103 found - processing....");
		Mt103Model mt103 = mt103List.get(0);

		List<Account> accounts = accountRepository.findByAccountNumber(mt103.getCreditorAccountNumber());
		if (accounts.isEmpty()) {
			return "accountNotFound";
		}
		Account creditorsAccount = accounts.get(0);
		creditorsAccount.setBalance(creditorsAccount.getBalance() + mt103.getTotal());
		System.out.println("added amount to creditors balance...");

		AccountAnalytics accountAnalytics = new AccountAnalytics(mt103.getDebtorBankSwift(), mt103.getDebtorBankAccountNumber(),
				mt103.getCreditorBankSwift(), mt103.getCreditorAccountNumber(), mt103.getDebtorInfo(),
				mt103.getPaymentPurpose(), mt103.getCreditorInfo(), mt103.getDateOfPayment(), mt103.getDateOfValue(),
				mt103.getDebtorAccountNumber(), mt103.getDebtorModel(), mt103.getDebtorReferenceNumber(),
				mt103.getCreditorAccountNumber(), mt103.getCreditorModel(), mt103.getCreditorReferenceNumber(),
				mt103.getTotal(), 0.0, mt103.getCurrency(), true);
		accountAnalyticsRepository.save(accountAnalytics);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		Date today = calendar.getTime();

		boolean foundDab = false;
		for (DailyAccountBalance dab : creditorsAccount.getDailyAccountBalances()) {
			System.out.println("searching through dabs....");
			calendar.setTime(dab.getDate());
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			Date dabDate = calendar.getTime();

			if (today.equals(dabDate)) {
				System.out.println("adding to existing dab...");
				dab.getAccountAnalytics().add(accountAnalytics);
				dab.setPreviousBalance(dab.getNewBalance());
				dab.setNewBalance(dab.getNewBalance() + mt103.getTotal());
				dab.setNumberOfDeposits(dab.getNumberOfDeposits() + 1);
				dailyAccountBalanceRepository.save(dab);
				accountAnalytics.setDailyAccountBalance(dab);
				foundDab = true;
				break;
			}
		}
		if (!foundDab) {
			System.out.println("creating new dab........");
			DailyAccountBalance newDab = new DailyAccountBalance(today, creditorsAccount.getBalance(), 0,
					creditorsAccount.getBalance() + mt103.getTotal(), 1, creditorsAccount);
			ArrayList<AccountAnalytics> accountAnalyticsList = new ArrayList<AccountAnalytics>();
			accountAnalyticsList.add(accountAnalytics);
			newDab.setAccountAnalytics(accountAnalyticsList);
			DailyAccountBalance newDabDB = dailyAccountBalanceRepository.save(newDab);
			accountAnalytics.setDailyAccountBalance(newDabDB);
		}
		accountRepository.save(creditorsAccount);
		accountAnalyticsRepository.save(accountAnalytics);

		return "OK";
	}

}

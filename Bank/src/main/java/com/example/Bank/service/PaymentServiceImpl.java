package com.example.Bank.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Bank.model.Account;
import com.example.Bank.model.AccountAnalytics;
import com.example.Bank.model.Bank;
import com.example.Bank.model.DailyAccountBalance;
import com.example.Bank.model.Mt102Model;
import com.example.Bank.model.Mt103Model;
import com.example.Bank.model.SinglePaymentModel;
import com.example.Bank.repository.AccountAnalyticsRepository;
import com.example.Bank.repository.AccountRepository;
import com.example.Bank.repository.BankRepository;
import com.example.Bank.repository.DailyAccountBalanceRepository;
import com.example.Bank.repository.Mt102Repository;
import com.example.Bank.repository.Mt103ModelRepository;
import com.example.Bank.repository.SinglePaymentRepository;
import com.example.service.bankstatement.BankStatement;
import com.example.service.bankstatement.BankStatement.BankStatementItem;
import com.example.service.bankstatementrequest.BankStatementRequest;
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
	
	@Autowired
	private SinglePaymentRepository singlePaymentRepository;
	
	@Autowired
	private Mt102Repository mt102Repository;
	
	@Autowired
	private Mt103ModelRepository mt103ModelRepository;
	
	@Override
	public String createDebtorAccountAnalytics(PaymentOrder paymentOrder, boolean isClearing) {
		System.out.println("Creating debtor account analytics....");
		Date dateOfPayment = paymentOrder.getDateOfPayment().toGregorianCalendar().getTime();
		Date dateOfValue = paymentOrder.getDateOfValue().toGregorianCalendar().getTime();
		double amount = paymentOrder.getAmount().doubleValue();
		double reservedFunds = 0;
		if(isClearing){
			amount = 0;
			reservedFunds = paymentOrder.getAmount().doubleValue();
		}
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
				reservedFunds,
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
        System.out.println("Seaching through dabs - date: "+poDate);
		for (DailyAccountBalance dab : debtorsAccount.getDailyAccountBalances()) {
			Date dabDate = dab.getDate();
			calendar.setTime(poDate);
	        calendar.set(Calendar.MILLISECOND, 0);
	        calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.HOUR, 0);
	        dabDate = calendar.getTime();
			
	        if(dabDate.equals(poDate)){
	        	System.out.println("Found dab!");
	        	dab.setPreviousBalance(dab.getNewBalance());
	        	dab.setNewBalance(dab.getNewBalance() - paymentOrder.getAmount().doubleValue());
	        	dab.getAccountAnalytics().add(debtorsAccountAnalytics);
	        	debtorsAccountAnalytics.setDailyAccountBalance(dab);
	        	dab.setNumberOfWithdrawals(dab.getNumberOfWithdrawals() + 1);
	        	dailyAccountBalanceRepository.save(dab);
				System.out.println("new Balance:"+dab.getNewBalance());
				System.out.println("previous Balance:"+dab.getPreviousBalance());
	        	dabExists = true;
	        	break;
	        }
		}
		if(!dabExists) {
			System.out.println("Didn't find dab...creating a new one!");
			DailyAccountBalance newDab = new DailyAccountBalance(poDate, debtorsAccount.getBalance(),
					1, debtorsAccount.getBalance() - amount, 0, debtorsAccount);
			if(isClearing) {
				newDab.setNewBalance(debtorsAccount.getBalance() - reservedFunds);
			}
			DailyAccountBalance newDabDB = dailyAccountBalanceRepository.save(newDab);
			DailyAccountBalance newDab2 = dailyAccountBalanceRepository.findOne(newDabDB.getId());
			debtorsAccountAnalytics.setDailyAccountBalance(newDab2);
			newDab2.getAccountAnalytics().add(debtorsAccountAnalytics);
			debtorsAccount.getDailyAccountBalances().add(newDab2);
			System.out.println("new Balance:"+newDab2.getNewBalance());
			System.out.println("previous Balance:"+newDab2.getPreviousBalance());
			dailyAccountBalanceRepository.save(newDabDB);
		}
		if(isClearing){
			debtorsAccount.setReservedFunds(debtorsAccount.getReservedFunds()+reservedFunds);
		} else {
			debtorsAccount.setBalance(debtorsAccount.getBalance() - amount);
		}
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
		System.out.println("Creating creditor account analytics...");
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
				0,
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
	        	dab.setPreviousBalance(dab.getNewBalance());
	        	dab.setNewBalance(dab.getNewBalance() + paymentOrder.getAmount().doubleValue());
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
		mt103.setMessageId(UUID.randomUUID().toString());
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
		System.out.println("kreirao mt103");
		Mt103Model mt103Model = new Mt103Model(mt103);
		mt103ModelRepository.save(mt103Model);
		
		//reserveFunds(mt103.getDebtor().getAccountNumber(), mt103.getTotal().doubleValue());
		
		return mt103;
	}

	@Override
	public String createMT102Model(PaymentOrder paymentOrder, SinglePaymentModel singlePaymentModel) {
		Mt102Model mt102 = new Mt102Model();
		mt102.setMessageId(UUID.randomUUID().toString());
		System.out.println("creating mt102 with uuid:"+mt102.getMessageId());
		mt102.setDateOfPayment(paymentOrder.getDateOfPayment().toGregorianCalendar().getTime());
		mt102.setDateOfValue(paymentOrder.getDateOfValue().toGregorianCalendar().getTime());
		List<Account> debtorList = accountRepository.findByAccountNumber(paymentOrder.getDebtor().getAccountNumber());
		if(debtorList.isEmpty()){
			return "debtorsAccountNotFound";
		}
		Account debtorsAccount = debtorList.get(0);
		Bank debtorsBank = debtorsAccount.getBank();
		mt102.setDebtorAccountNumber(debtorsBank.getAccountNumber());
		mt102.setDebtorSwift(debtorsBank.getSWIFTcode());
		String bankCode = paymentOrder.getCreditor().getAccountNumber().substring(0, 3);
		List<Bank> banks = bankRepository.findByCode(Integer.parseInt(bankCode));
		if(banks.isEmpty()){
			return "creditorsBankNotFound";
		}
		Bank creditorsBank = banks.get(0);
		mt102.setCreditorAccountNumber(creditorsBank.getAccountNumber());
		mt102.setCreditorSwift(creditorsBank.getSWIFTcode());
		mt102.setTotal(singlePaymentModel.getTotal());
		mt102.setCurrency(singlePaymentModel.getCurrency());
		mt102.setSent(false);
		ArrayList<SinglePaymentModel> list = new ArrayList<SinglePaymentModel>();
		list.add(singlePaymentModel);
		mt102.setSinglePaymentModels(list);
		singlePaymentModel.setMt102(mt102);
		mt102Repository.save(mt102);
		singlePaymentRepository.save(singlePaymentModel);
		System.out.println("created mt102...");
		return "OK";
	}

	@Override
	public String createSinglePaymentForMt012(PaymentOrder paymentOrder) {
		SinglePaymentModel singlePayment = new SinglePaymentModel();
		singlePayment.setPaymentId("?");
		singlePayment.setPaymentPurpose(paymentOrder.getPaymentPurpose());
		singlePayment.setDateOfOrder(paymentOrder.getDateOfPayment().toGregorianCalendar().getTime());
		singlePayment.setTotal(paymentOrder.getAmount().doubleValue());
		singlePayment.setCurrency(paymentOrder.getCurrency());

		singlePayment.setCreditorInfo(paymentOrder.getCreditor().getInfo());
		singlePayment.setCreditorAccountNumber(paymentOrder.getCreditor().getAccountNumber());
		singlePayment.setCreditorModel(paymentOrder.getCreditor().getModel());
		singlePayment.setCreditorReferenceNumber(paymentOrder.getCreditor().getReferenceNumber());
		
		singlePayment.setDebtorInfo(paymentOrder.getDebtor().getInfo());
		singlePayment.setDebtorAccountNumber(paymentOrder.getDebtor().getAccountNumber());
		singlePayment.setDebtorModel(paymentOrder.getDebtor().getModel());
		singlePayment.setDebtorReferenceNumber(paymentOrder.getDebtor().getReferenceNumber());
		
		String bankCode = paymentOrder.getCreditor().getAccountNumber().substring(0, 3);
		List<Bank> banks = bankRepository.findByCode(Integer.parseInt(bankCode));
		if(banks.isEmpty()){
			return "bankNotFound";
		}
		Bank creditorsBank = banks.get(0);
		List<Mt102Model> mt102s = mt102Repository
								.findByCreditorSwiftAndSent(creditorsBank.getSWIFTcode(), false);
		singlePaymentRepository.save(singlePayment);
		//reserveFunds(paymentOrder.getDebtor().getAccountNumber(), paymentOrder.getAmount().doubleValue());
		if(mt102s.isEmpty()){
			System.out.println("creating new mt102...");
			 createMT102Model(paymentOrder, singlePayment);
			 
		} else {
			System.out.println("adding to existing mt102...");
			Mt102Model mt102 = mt102s.get(0);
			mt102.getSinglePaymentModels().add(singlePayment);
			mt102.setTotal(mt102.getTotal() + singlePayment.getTotal());
			singlePayment.setMt102(mt102);
			mt102Repository.save(mt102);
			singlePaymentRepository.save(singlePayment);
			if(mt102.getSinglePaymentModels().size() >= 2){
				System.out.println(">2......");
				return "readyToSend";
			}
			System.out.println("<2.....");
			
		}
		return "OK";
	}

	@Override
	public String reserveFunds(String accountNumber, double amount) {
		List<Account> accounts = accountRepository.findByAccountNumber(accountNumber);
		if(accounts.isEmpty()){
			return "accountNumberNotFound";
		} 
		Account account = accounts.get(0);
		account.setReservedFunds(account.getReservedFunds() + amount);
		accountRepository.save(account);
		return "OK";
	}

	@Override
	public String getBanksSwift(String clientsAccountNumber) {
		String bankCode = clientsAccountNumber.substring(0, 3);
		List<Bank> banks = bankRepository.findByCode(Integer.parseInt(bankCode));
		if(banks.isEmpty()){
			return "bankNotFound";
		}
		return banks.get(0).getSWIFTcode();
	}

	@Override
	public BankStatement getBankStatement(BankStatementRequest bankStatementRequest) {
		System.out.println("creating a bank statement for date and account :");
		System.out.println(bankStatementRequest.getDate().toGregorianCalendar().getTime());
		System.out.println(bankStatementRequest.getAccountNumber());

		List<Account> accounts = accountRepository.findByAccountNumber(bankStatementRequest.getAccountNumber());
		if (accounts.isEmpty()) {
			System.out.println("Nalog nije pronadjen");
			return null;
		}
		Account account = accounts.get(0);
		
		Date bsrDate = bankStatementRequest.getDate().toGregorianCalendar().getTime();
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(bsrDate);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        bsrDate = calendar.getTime();
        
        BankStatement bankStatement = new BankStatement();
        
        double totalWidrawn = 0;
        double totalDeposited = 0;
        boolean foundDab = false;
        ArrayList<BankStatementItem> bankStatementItems = new ArrayList<BankStatementItem>();
		for (DailyAccountBalance dab : account.getDailyAccountBalances()) {
			Date dabDate = dab.getDate();
			calendar.setTime(dabDate);
	        calendar.set(Calendar.MILLISECOND, 0);
	        calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.HOUR, 0);
	        dabDate = calendar.getTime();
	        if (dabDate.equals(bsrDate)) {
	        	System.out.println("found dab...");
	        	foundDab = true;
	        	for (AccountAnalytics accountAnalytic : dab.getAccountAnalytics()) {
	        		BankStatementItem bankStatementItem = new BankStatementItem();
	        		
	        		com.example.service.bankstatement.TCompanyData creditorData =  new com.example.service.bankstatement.TCompanyData();
	        		creditorData.setAccountNumber(accountAnalytic.getCreditorsAccountNumber());
	        		creditorData.setInfo(accountAnalytic.getCreditor());
	        		creditorData.setModel(accountAnalytic.getCreditorsModel());
	        		creditorData.setReferenceNumber(accountAnalytic.getCreditorsReferenceNumber());
	        		
	        		com.example.service.bankstatement.TCompanyData debtorData =  new com.example.service.bankstatement.TCompanyData();
	        		debtorData.setAccountNumber(accountAnalytic.getDebtorsAccountNumber());
	        		debtorData.setInfo(accountAnalytic.getDebtor());
	        		debtorData.setModel(accountAnalytic.getDebtorsModel());
	        		debtorData.setReferenceNumber(accountAnalytic.getDebtorsReferenceNumber());
	        		
	        		GregorianCalendar c = new GregorianCalendar();
	        		c.setTime(accountAnalytic.getDateOfPaymentOrder());
	        		XMLGregorianCalendar dateOfPayment;
					try {
						dateOfPayment = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
						bankStatementItem.setDateOfPayment(dateOfPayment);
					} catch (DatatypeConfigurationException e1) {
						e1.printStackTrace();
					}
	        		c.setTime(accountAnalytic.getDateOfValue());
	        		XMLGregorianCalendar dateOfValue;
					try {
						dateOfValue = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
						bankStatementItem.setDateOfValue(dateOfValue);
					} catch (DatatypeConfigurationException e) {
						e.printStackTrace();
					}
	        		
	        		
	        		bankStatementItem.setCreditor(creditorData);
	        		bankStatementItem.setDebtor(debtorData);
	        		bankStatementItem.setDirection(accountAnalytic.isReceived() ? "i" : "o");
	        		bankStatementItem.setPaymentPurpose("Payment based on an invoice");
	        		bankStatementItem.setTotal(new BigDecimal(accountAnalytic.getAmount()));
	        		
	        		if (accountAnalytic.isReceived()) {
	        			totalDeposited += accountAnalytic.getAmount();
	        		}
	        		else {
	        			totalWidrawn += accountAnalytic.getAmount();
	        		}
	        		
	        		bankStatementItems.add(bankStatementItem);
				}
	        	bankStatement.setNewBalance(new BigDecimal(dab.getNewBalance()));
	        	bankStatement.setNumberOfDeposits(dab.getNumberOfDeposits());
	    		bankStatement.setNumberOfWithdrawals(dab.getNumberOfWithdrawals());
	    		bankStatement.setPreviousBalance(new BigDecimal(dab.getPreviousBalance()));
	        }
		}
		
		bankStatement.setAccountNumber(bankStatementRequest.getAccountNumber());
		bankStatement.setDate(bankStatementRequest.getDate());		
		bankStatement.setSectionNumber(bankStatementRequest.getSectionNumber());
		bankStatement.setTotalDeposited(new BigDecimal(totalDeposited));
		bankStatement.setTotalWithdrawn(new BigDecimal(totalWidrawn));
		bankStatement.setDate(bankStatementRequest.getDate());
		if(!foundDab){
			System.out.println("didn't find dab");
			bankStatement.setNewBalance(new BigDecimal(account.getBalance()));
        	bankStatement.setNumberOfDeposits(0);
    		bankStatement.setNumberOfWithdrawals(0);
    		bankStatement.setPreviousBalance(new BigDecimal(account.getBalance()));
		}
		return bankStatement;
	}

	

	
}

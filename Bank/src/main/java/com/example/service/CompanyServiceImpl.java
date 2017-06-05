package com.example.service;

import javax.jws.WebService;

import com.example.service.bankstatement.BankStatement;
import com.example.service.bankstatement.BankStatement.BankStatementItem;
import com.example.service.bankstatementrequest.BankStatementRequest;
import com.example.service.paymentorder.PaymentOrder;

@WebService(endpointInterface = "com.example.service.CompanyService")
public class CompanyServiceImpl implements CompanyService{

	@Override
	public BankStatement processBankStatementRequest(BankStatementRequest request) {
		System.out.println(request.getAccountNumber());
		System.out.println(request.getDate());
		System.out.println(request.getSectionNumber());
		BankStatement bankStatement = new BankStatement();
		bankStatement.setAccountNumber("test acc");
		
		BankStatementItem bankStatementItem1 =  new BankStatementItem();
		bankStatementItem1.setPaymentPurpose("svrhaa1");
		BankStatementItem bankStatementItem2 =  new BankStatementItem();
		bankStatementItem2.setPaymentPurpose("svrhaa2");
		bankStatement.getBankStatementItem().add(bankStatementItem1);
		bankStatement.getBankStatementItem().add(bankStatementItem2);
		return bankStatement;
	}

	@Override
	public String processPaymentOrder(PaymentOrder paymentOrder) {
		System.out.println(paymentOrder.getMessageId());
		System.out.println("cr info: "+paymentOrder.getCreditor().getInfo());
		System.out.println("db info: "+paymentOrder.getDebtor().getInfo());
		System.out.println("curr: "+paymentOrder.getCurrency());
		System.out.println("urg: "+paymentOrder.isUrgent());
		System.out.println("date of v:"+paymentOrder.getDateOfValue());
		return "testReturnValueAll";
	}

}

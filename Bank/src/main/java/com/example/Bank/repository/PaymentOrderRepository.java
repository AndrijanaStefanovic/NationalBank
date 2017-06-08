package com.example.Bank.repository;

import org.springframework.data.repository.Repository;

import com.example.Bank.model.PaymentOrderModel;

public interface PaymentOrderRepository extends Repository<PaymentOrderModel, Long> {

	public PaymentOrderModel save(PaymentOrderModel entity);
}

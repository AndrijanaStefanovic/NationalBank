package com.example.Company.repository;

import org.springframework.data.repository.Repository;

import com.example.Company.model.PaymentOrderModel;

public interface PaymentOrderRepository extends Repository<PaymentOrderModel, Long>{

	public PaymentOrderModel save(PaymentOrderModel entity);
}

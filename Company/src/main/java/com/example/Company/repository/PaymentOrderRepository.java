package com.example.Company.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import com.example.Company.model.PaymentOrderModel;

public interface PaymentOrderRepository extends Repository<PaymentOrderModel, Long>{

	public PaymentOrderModel save(PaymentOrderModel entity);

	public Page<PaymentOrderModel> findAll(Pageable pageable);
}

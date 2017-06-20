package com.example.Bank.repository;

import org.springframework.data.repository.Repository;

import com.example.Bank.model.SinglePaymentModel;

public interface SinglePaymentRepository extends Repository<SinglePaymentModel, Long>{

	public SinglePaymentModel save(SinglePaymentModel entity);
}

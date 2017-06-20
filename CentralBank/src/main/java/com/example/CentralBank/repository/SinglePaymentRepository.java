package com.example.CentralBank.repository;

import org.springframework.data.repository.Repository;

import com.example.CentralBank.model.SinglePaymentModel;

public interface SinglePaymentRepository extends Repository<SinglePaymentModel, Long>{

	public SinglePaymentModel save(SinglePaymentModel entity);
}

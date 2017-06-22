package com.example.Company.service;

import com.example.Company.model.PaymentOrderModel;
import com.example.Company.repository.PaymentOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PaymentOrderServiceImpl implements PaymentOrderService {

    @Autowired
    PaymentOrderRepository paymentOrderRepository;

    @Override
    public Collection<PaymentOrderModel> getAllPaymentOrders() {
        return paymentOrderRepository.findAll(null).getContent();
    }
}

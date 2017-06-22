package com.example.Company.controller;

import com.example.Company.model.PaymentOrderModel;
import com.example.Company.service.PaymentOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class PaymentOrderController {

    @Autowired
    private PaymentOrderService paymentOrderService;

    @RequestMapping(
            value = "/paymentOrder/getAllPaymentOrders",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Collection<PaymentOrderModel> getAllPaymentOrders() {
        return paymentOrderService.getAllPaymentOrders();
    }
}

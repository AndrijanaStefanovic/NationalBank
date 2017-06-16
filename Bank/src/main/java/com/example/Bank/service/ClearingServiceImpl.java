package com.example.Bank.service;

import com.example.Bank.model.Bank;
import com.example.Bank.repository.BankRepository;
import com.example.service.mt102.Mt102;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.util.List;

@Service
public class ClearingServiceImpl extends WebServiceGatewaySupport implements ClearingService{

    @Autowired
    private BankRepository bankRepository;

    @Override
    public String processMT102(Mt102 mt102) {

        return "OK";
    }
}

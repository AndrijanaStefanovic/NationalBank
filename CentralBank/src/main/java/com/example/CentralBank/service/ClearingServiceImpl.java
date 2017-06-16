package com.example.CentralBank.service;

import com.example.CentralBank.model.Bank;
import com.example.CentralBank.repository.BankRepository;
import com.example.CentralBank.service.jaxws.ProcessMT900;
import com.example.CentralBank.service.jaxws.ProcessMT900Response;
import com.example.service.mt102.Mt102;
import com.example.service.mt900.Mt900;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.util.List;

@Service
public class ClearingServiceImpl extends WebServiceGatewaySupport implements ClearingService{

    @Autowired
    private BankRepository bankRepository;

    @Override
    public String processMT102(Mt102 mt102) {

        List<Bank> crb = bankRepository.findBySwiftCode(mt102.getCreditorSwift());
        if(crb.isEmpty()){
            return "creditorsBankSWIFTError";
        }

        List<Bank> dbb = bankRepository.findBySwiftCode(mt102.getDebtorSwift());
        if(dbb.isEmpty()){
            return "debtorsBankSWIFTError";
        }
        Bank creditorsBank = crb.get(0);
        System.out.println(creditorsBank.getName());
        Bank debtorsBank = dbb.get(0);
        System.out.println(debtorsBank.getName());

        return "OK";
    }
}

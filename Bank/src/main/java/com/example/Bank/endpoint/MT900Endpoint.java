package com.example.Bank.endpoint;

import com.example.Bank.model.Bank;
import com.example.Bank.repository.BankRepository;
import com.example.Bank.service.jaxws.ProcessMT900;
import com.example.Bank.service.jaxws.ProcessMT900Response;
import com.example.service.bankstatement.BankStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class MT900Endpoint {

    private static final String NAMESPACE_URI = "http://service.Bank.example.com/";

    @Autowired
    BankRepository bankRepository;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "processMT900")
    @ResponsePayload
    public ProcessMT900Response processMT900(@RequestPayload ProcessMT900 mt900) {
        ProcessMT900Response response = new ProcessMT900Response();
        System.out.println(mt900.getArg0().getClass().getSimpleName());

        List<Bank> banks = bankRepository.findAll();

        for (Bank bank : banks) {
            System.out.println("**************************************");
            System.out.println("testing database");
            System.out.println(bank.getAccountNumber().toString());
        }

        response.setReturn("test return mt900");

        //TODO Ovde odraditi skidanje para sa racuna te banke
        //Skini pare sa racuna (odraditi)

        System.out.println("Treba skinuti pare sa racuna");

        return response;
    }
}

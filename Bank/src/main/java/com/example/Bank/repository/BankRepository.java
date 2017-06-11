package com.example.Bank.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.example.Bank.model.Bank;

public interface BankRepository extends Repository<Bank, Long> {

	public List<Bank> findByCode(int code); 
}

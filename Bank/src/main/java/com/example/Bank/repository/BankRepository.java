package com.example.Bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import com.example.Bank.model.Bank;

public interface BankRepository extends JpaRepository<Bank, Long> {

	public List<Bank> findByCode(int code);

	public List<Bank> findById(Long id);
	
	public List<Bank> findBySwiftCode(String swift);
}

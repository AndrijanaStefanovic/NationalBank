package com.example.Bank.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.example.Bank.model.Mt102Model;

public interface Mt102Repository extends Repository<Mt102Model, Long>{

	public Mt102Model save(Mt102Model entity);
	
	public List<Mt102Model> findByCreditorSwiftAndSent(String creditorSwift, boolean sent);
	
	public Mt102Model findOne(Long id);
}

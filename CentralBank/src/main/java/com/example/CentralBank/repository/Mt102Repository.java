package com.example.CentralBank.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.example.CentralBank.model.Mt102Model;

public interface Mt102Repository extends Repository<Mt102Model, Long>{

	public Mt102Model save(Mt102Model entity);
	
	public List<Mt102Model> findByCleared(boolean cleared);
}

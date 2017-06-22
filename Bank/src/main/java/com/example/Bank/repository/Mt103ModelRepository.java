package com.example.Bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Bank.model.Mt103Model;

public interface Mt103ModelRepository extends JpaRepository<Mt103Model, Long>{

	List<Mt103Model> findByMessageId(String messageId);

}

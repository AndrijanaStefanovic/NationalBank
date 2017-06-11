package com.example.Bank.repository;

import org.springframework.data.repository.Repository;

import com.example.Bank.model.AccountAnalytics;

public interface AccountAnalyticsRepository extends Repository<AccountAnalytics, Long> {

	public AccountAnalytics save(AccountAnalytics entity);
}

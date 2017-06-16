package com.example.Bank.repository;

import com.example.Bank.model.ClearingCounter;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Alek on 6/17/2017.
 */
public interface ClearingCounterRepository extends JpaRepository<ClearingCounter, Long> {
}

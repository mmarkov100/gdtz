package com.bank_deposits.demo.repos;

import com.bank_deposits.demo.entities.Deposit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit, Integer> {
    List<Deposit> findByClientIdAndBankIdAndOpenDateBetweenAndPercentBetweenAndTimeMonth(
            Integer clientId, Integer bankId, LocalDate startDate, LocalDate endDate,
            Double minPercent, Double maxPercent, Integer timeMonth, Sort sort);
}
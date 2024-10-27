package com.bank_deposits.demo.repos;

import com.bank_deposits.demo.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Integer> {

}
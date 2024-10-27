package com.bank_deposits.demo.repos;

import com.bank_deposits.demo.entities.Client;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository
        extends JpaRepository<Client, Integer>{
    List<Client> findByNameContainingAndShortNameContainingAndEmailContainingAndLegalForm(
            String name, String shortName, String email, Client.LegalForm legalForm, Sort sort);
}
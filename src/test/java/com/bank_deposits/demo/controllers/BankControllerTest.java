package com.bank_deposits.demo.controllers;

import com.bank_deposits.demo.entities.Bank;
import com.bank_deposits.demo.repos.BankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BankControllerTest {

    @InjectMocks
    private BankController bankController;

    @Mock
    private BankRepository bankRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBanks() {
        Bank bank1 = new Bank(1, "Bank A", "BIC123");
        Bank bank2 = new Bank(2, "Bank B", "BIC456");
        List<Bank> banks = new ArrayList<>();
        banks.add(bank1);
        banks.add(bank2);

        when(bankRepository.findAll()).thenReturn(banks);

        List<Bank> result = bankController.getAllBanks();

        assertEquals(2, result.size());
        verify(bankRepository, times(1)).findAll();
    }

    @Test
    void testGetBankById_Found() {
        Bank bank = new Bank(1, "Bank A", "BIC123");

        when(bankRepository.findById(1)).thenReturn(Optional.of(bank));

        ResponseEntity<Bank> result = bankController.getClientById(1);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(bank, result.getBody());
        verify(bankRepository, times(1)).findById(1);
    }

    @Test
    void testGetBankById_NotFound() {
        when(bankRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Bank> result = bankController.getClientById(1);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(bankRepository, times(1)).findById(1);
    }

    @Test
    void testAddBank() {
        BankController.NewBank newBank = new BankController.NewBank("Bank A", "BIC123");

        bankController.addClient(newBank);

        verify(bankRepository, times(1)).save(any(Bank.class));
    }

    @Test
    void testUpdateBank_Found() {
        Bank existingBank = new Bank(1, "Bank A", "BIC123");
        Bank updatedBank = new Bank(1, "Bank A Updated", "BIC789");

        when(bankRepository.existsById(1)).thenReturn(true);
        when(bankRepository.save(any(Bank.class))).thenReturn(updatedBank);

        ResponseEntity<Bank> result = bankController.updateBank(1, updatedBank);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updatedBank, result.getBody());
        verify(bankRepository, times(1)).existsById(1);
        verify(bankRepository, times(1)).save(any(Bank.class));
    }

    @Test
    void testUpdateBank_NotFound() {
        Bank updatedBank = new Bank(1, "Bank A Updated", "BIC789");

        when(bankRepository.existsById(1)).thenReturn(false);

        ResponseEntity<Bank> result = bankController.updateBank(1, updatedBank);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(bankRepository, times(1)).existsById(1);
        verify(bankRepository, never()).save(any(Bank.class));
    }

    @Test
    void testDeleteBank_Found() {
        when(bankRepository.existsById(1)).thenReturn(true);

        ResponseEntity<Void> result = bankController.deleteBank(1);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(bankRepository, times(1)).existsById(1);
        verify(bankRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteBank_NotFound() {
        when(bankRepository.existsById(1)).thenReturn(false);

        ResponseEntity<Void> result = bankController.deleteBank(1);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(bankRepository, times(1)).existsById(1);
        verify(bankRepository, never()).deleteById(1);
    }
}


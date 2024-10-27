package com.bank_deposits.demo.controllers;

import com.bank_deposits.demo.entities.Bank;
import com.bank_deposits.demo.entities.Client;
import com.bank_deposits.demo.entities.Deposit;
import com.bank_deposits.demo.repos.DepositRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DepositControllerTest {

    @InjectMocks
    private DepositController depositController;

    @Mock
    private DepositRepository depositRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDeposits() {
        Deposit deposit1 = new Deposit(1, new Client(), new Bank(), LocalDate.now(), 5.0, 12);
        Deposit deposit2 = new Deposit(2, new Client(), new Bank(), LocalDate.now(), 6.0, 24);
        List<Deposit> deposits = new ArrayList<>();
        deposits.add(deposit1);
        deposits.add(deposit2);

        when(depositRepository.findAll(Sort.by("id"))).thenReturn(deposits);

        List<Deposit> result = depositController.getDeposits(null, null, null, null, null, null, null, "id");

        assertEquals(2, result.size());
        verify(depositRepository, times(1)).findAll(Sort.by("id"));
    }

    @Test
    void testGetDepositById_Found() {
        Deposit deposit = new Deposit(1, new Client(), new Bank(), LocalDate.now(), 5.0, 12);

        when(depositRepository.findById(1)).thenReturn(Optional.of(deposit));

        ResponseEntity<Deposit> result = depositController.getDepositById(1);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(deposit, result.getBody());
        verify(depositRepository, times(1)).findById(1);
    }

    @Test
    void testGetDepositById_NotFound() {
        when(depositRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Deposit> result = depositController.getDepositById(1);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(depositRepository, times(1)).findById(1);
    }

    @Test
    void testAddDeposit() {
        DepositController.NewDeposit newDeposit = new DepositController.NewDeposit(new Client(), new Bank(), LocalDate.now(), 5.0, 12);

        depositController.addDeposit(newDeposit);

        verify(depositRepository, times(1)).save(any(Deposit.class));
    }

    @Test
    void testUpdateDeposit_Found() {
        Deposit existingDeposit = new Deposit(1, new Client(), new Bank(), LocalDate.now(), 5.0, 12);
        Deposit updatedDeposit = new Deposit(1, new Client(), new Bank(), LocalDate.now(), 6.0, 24);

        when(depositRepository.existsById(1)).thenReturn(true);
        when(depositRepository.save(any(Deposit.class))).thenReturn(updatedDeposit);

        ResponseEntity<Deposit> result = depositController.updateDeposit(1, updatedDeposit);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updatedDeposit, result.getBody());
        verify(depositRepository, times(1)).existsById(1);
        verify(depositRepository, times(1)).save(any(Deposit.class));
    }

    @Test
    void testUpdateDeposit_NotFound() {
        Deposit updatedDeposit = new Deposit(1, new Client(), new Bank(), LocalDate.now(), 6.0, 24);

        when(depositRepository.existsById(1)).thenReturn(false);

        ResponseEntity<Deposit> result = depositController.updateDeposit(1, updatedDeposit);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(depositRepository, times(1)).existsById(1);
        verify(depositRepository, never()).save(any(Deposit.class));
    }

    @Test
    void testDeleteDeposit_Found() {
        when(depositRepository.existsById(1)).thenReturn(true);

        ResponseEntity<Void> result = depositController.deleteDeposit(1);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(depositRepository, times(1)).existsById(1);
        verify(depositRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteDeposit_NotFound() {
        when(depositRepository.existsById(1)).thenReturn(false);

        ResponseEntity<Void> result = depositController.deleteDeposit(1);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(depositRepository, times(1)).existsById(1);
        verify(depositRepository, never()).deleteById(1);
    }
}


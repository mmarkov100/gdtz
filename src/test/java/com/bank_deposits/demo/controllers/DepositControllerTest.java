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
        // Данные для теста
        Deposit deposit1 = new Deposit(1, new Client(), new Bank(), LocalDate.now(), 5.0, 12);
        Deposit deposit2 = new Deposit(2, new Client(), new Bank(), LocalDate.now(), 6.0, 24);
        List<Deposit> deposits = new ArrayList<>();
        deposits.add(deposit1);
        deposits.add(deposit2);

        // Имитация поведения репозитория
        when(depositRepository.findAll(Sort.by("id"))).thenReturn(deposits);

        // Вызов метода контроллера
        List<Deposit> result = depositController.getDeposits(null, null, null, null, null, null, null, "id");

        // Проверка результата
        assertEquals(2, result.size());
        verify(depositRepository, times(1)).findAll(Sort.by("id"));
    }

    @Test
    void testGetDepositById_Found() {
        // Данные для теста
        Deposit deposit = new Deposit(1, new Client(), new Bank(), LocalDate.now(), 5.0, 12);

        // Имитация поведения репозитория
        when(depositRepository.findById(1)).thenReturn(Optional.of(deposit));

        // Вызов метода контроллера
        ResponseEntity<Deposit> result = depositController.getDepositById(1);

        // Проверка результата
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(deposit, result.getBody());
        verify(depositRepository, times(1)).findById(1);
    }

    @Test
    void testGetDepositById_NotFound() {
        // Имитация поведения репозитория
        when(depositRepository.findById(1)).thenReturn(Optional.empty());

        // Вызов метода контроллера
        ResponseEntity<Deposit> result = depositController.getDepositById(1);

        // Проверка результата
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(depositRepository, times(1)).findById(1);
    }

    @Test
    void testAddDeposit() {
        // Данные для теста
        DepositController.NewDeposit newDeposit = new DepositController.NewDeposit(new Client(), new Bank(), LocalDate.now(), 5.0, 12);

        // Вызов метода контроллера
        depositController.addDeposit(newDeposit);

        // Проверка взаимодействия с репозиторием
        verify(depositRepository, times(1)).save(any(Deposit.class));
    }

    @Test
    void testUpdateDeposit_Found() {
        // Данные для теста
        Deposit existingDeposit = new Deposit(1, new Client(), new Bank(), LocalDate.now(), 5.0, 12);
        Deposit updatedDeposit = new Deposit(1, new Client(), new Bank(), LocalDate.now(), 6.0, 24);

        // Имитация поведения репозитория
        when(depositRepository.existsById(1)).thenReturn(true);
        when(depositRepository.save(any(Deposit.class))).thenReturn(updatedDeposit);

        // Вызов метода контроллера
        ResponseEntity<Deposit> result = depositController.updateDeposit(1, updatedDeposit);

        // Проверка результата
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updatedDeposit, result.getBody());
        verify(depositRepository, times(1)).existsById(1);
        verify(depositRepository, times(1)).save(any(Deposit.class));
    }

    @Test
    void testUpdateDeposit_NotFound() {
        // Данные для теста
        Deposit updatedDeposit = new Deposit(1, new Client(), new Bank(), LocalDate.now(), 6.0, 24);

        // Имитация поведения репозитория
        when(depositRepository.existsById(1)).thenReturn(false);

        // Вызов метода контроллера
        ResponseEntity<Deposit> result = depositController.updateDeposit(1, updatedDeposit);

        // Проверка результата
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(depositRepository, times(1)).existsById(1);
        verify(depositRepository, never()).save(any(Deposit.class));
    }

    @Test
    void testDeleteDeposit_Found() {
        // Имитация поведения репозитория
        when(depositRepository.existsById(1)).thenReturn(true);

        // Вызов метода контроллера
        ResponseEntity<Void> result = depositController.deleteDeposit(1);

        // Проверка результата
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(depositRepository, times(1)).existsById(1);
        verify(depositRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteDeposit_NotFound() {
        // Имитация поведения репозитория
        when(depositRepository.existsById(1)).thenReturn(false);

        // Вызов метода контроллера
        ResponseEntity<Void> result = depositController.deleteDeposit(1);

        // Проверка результата
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(depositRepository, times(1)).existsById(1);
        verify(depositRepository, never()).deleteById(1);
    }
}


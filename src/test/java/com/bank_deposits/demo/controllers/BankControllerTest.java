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
        // Данные для теста
        Bank bank1 = new Bank(1, "Bank A", "BIC123");
        Bank bank2 = new Bank(2, "Bank B", "BIC456");
        List<Bank> banks = new ArrayList<>();
        banks.add(bank1);
        banks.add(bank2);

        // Имитация поведения репозитория
        when(bankRepository.findAll()).thenReturn(banks);

        // Вызов метода контроллера
        List<Bank> result = bankController.getAllBanks();

        // Проверка результата
        assertEquals(2, result.size());
        verify(bankRepository, times(1)).findAll();
    }

    @Test
    void testGetBankById_Found() {
        // Данные для теста
        Bank bank = new Bank(1, "Bank A", "BIC123");

        // Имитация поведения репозитория
        when(bankRepository.findById(1)).thenReturn(Optional.of(bank));

        // Вызов метода контроллера
        ResponseEntity<Bank> result = bankController.getClientById(1);

        // Проверка результата
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(bank, result.getBody());
        verify(bankRepository, times(1)).findById(1);
    }

    @Test
    void testGetBankById_NotFound() {
        // Имитация поведения репозитория
        when(bankRepository.findById(1)).thenReturn(Optional.empty());

        // Вызов метода контроллера
        ResponseEntity<Bank> result = bankController.getClientById(1);

        // Проверка результата
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(bankRepository, times(1)).findById(1);
    }

    @Test
    void testAddBank() {
        // Данные для теста
        BankController.NewBank newBank = new BankController.NewBank("Bank A", "BIC123");

        // Вызов метода контроллера
        bankController.addClient(newBank);

        // Проверка взаимодействия с репозиторием
        verify(bankRepository, times(1)).save(any(Bank.class));
    }

    @Test
    void testUpdateBank_Found() {
        // Данные для теста
        Bank existingBank = new Bank(1, "Bank A", "BIC123");
        Bank updatedBank = new Bank(1, "Bank A Updated", "BIC789");

        // Имитация поведения репозитория
        when(bankRepository.existsById(1)).thenReturn(true);
        when(bankRepository.save(any(Bank.class))).thenReturn(updatedBank);

        // Вызов метода контроллера
        ResponseEntity<Bank> result = bankController.updateBank(1, updatedBank);

        // Проверка результата
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updatedBank, result.getBody());
        verify(bankRepository, times(1)).existsById(1);
        verify(bankRepository, times(1)).save(any(Bank.class));
    }

    @Test
    void testUpdateBank_NotFound() {
        // Данные для теста
        Bank updatedBank = new Bank(1, "Bank A Updated", "BIC789");

        // Имитация поведения репозитория
        when(bankRepository.existsById(1)).thenReturn(false);

        // Вызов метода контроллера
        ResponseEntity<Bank> result = bankController.updateBank(1, updatedBank);

        // Проверка результата
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(bankRepository, times(1)).existsById(1);
        verify(bankRepository, never()).save(any(Bank.class));
    }

    @Test
    void testDeleteBank_Found() {
        // Имитация поведения репозитория
        when(bankRepository.existsById(1)).thenReturn(true);

        // Вызов метода контроллера
        ResponseEntity<Void> result = bankController.deleteBank(1);

        // Проверка результата
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(bankRepository, times(1)).existsById(1);
        verify(bankRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteBank_NotFound() {
        // Имитация поведения репозитория
        when(bankRepository.existsById(1)).thenReturn(false);

        // Вызов метода контроллера
        ResponseEntity<Void> result = bankController.deleteBank(1);

        // Проверка результата
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(bankRepository, times(1)).existsById(1);
        verify(bankRepository, never()).deleteById(1);
    }
}


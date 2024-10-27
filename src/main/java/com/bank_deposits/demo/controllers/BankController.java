package com.bank_deposits.demo.controllers;

import com.bank_deposits.demo.entities.Bank;
import com.bank_deposits.demo.repos.BankRepository;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/banks")
public class BankController {

    private final BankRepository bankRepository;

    public BankController(final BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    record NewBank (
            String name,
            String bic_number
    ){}

    @GetMapping
    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bank> getClientById(@PathVariable Integer id) {
        Optional<Bank> bank = bankRepository.findById(id);
        return bank.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public void addClient(@RequestBody BankController.NewBank request) {
        Bank bank = new Bank();
        bank.setName(request.name);
        bank.setBic_number(request.bic_number);
        bankRepository.save(bank);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bank> updateBank(@PathVariable Integer id, @RequestBody Bank bank) {
        if (!bankRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bank.setId(id);
        Bank updatedBank = bankRepository.save(bank);
        return ResponseEntity.ok(updatedBank);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBank(@PathVariable Integer id) {
        if (!bankRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bankRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
package com.bank_deposits.demo.controllers;

import com.bank_deposits.demo.entities.Bank;
import com.bank_deposits.demo.entities.Client;
import com.bank_deposits.demo.entities.Deposit;
import com.bank_deposits.demo.repos.DepositRepository;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/deposits")
public class DepositController {

    private final DepositRepository depositRepository;

    public DepositController(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

    record NewDeposit(Client client,
                     Bank bank,
                     LocalDate openDate,
                     Double percent,
                     Integer timeMonth) {}

    @GetMapping
    public List<Deposit> getDeposits(
            @RequestParam(required = false) Integer clientId,
            @RequestParam(required = false) Integer bankId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Double minPercent,
            @RequestParam(required = false) Double maxPercent,
            @RequestParam(required = false) Integer timeMonth,
            @RequestParam(required = false, defaultValue = "id") String sortBy) {

        Sort sort = Sort.by(sortBy);
        if (clientId != null || bankId != null || startDate != null || endDate != null || minPercent != null || maxPercent != null || timeMonth != null) {
            return depositRepository.findByClientIdAndBankIdAndOpenDateBetweenAndPercentBetweenAndTimeMonth(
                    clientId, bankId,
                    startDate != null ? startDate : LocalDate.of(1900, 1, 1),
                    endDate != null ? endDate : LocalDate.now(),
                    minPercent != null ? minPercent : 0.0,
                    maxPercent != null ? maxPercent : 100.0,
                    timeMonth,
                    sort
            );
        }
        return depositRepository.findAll(sort);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Deposit> getDepositById(@PathVariable Integer id) {
        Optional<Deposit> deposit = depositRepository.findById(id);
        return deposit.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public void addDeposit(@RequestBody NewDeposit request) {
        Deposit deposit = new Deposit();
        deposit.setClient(request.client);
        deposit.setBank(request.bank);
        deposit.setOpenDate(request.openDate);
        deposit.setPercent(request.percent);
        deposit.setTimeMonth(request.timeMonth);
        depositRepository.save(deposit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Deposit> updateDeposit(@PathVariable Integer id, @RequestBody Deposit deposit) {
        if (!depositRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        deposit.setId(id);
        Deposit updatedDeposit = depositRepository.save(deposit);
        return ResponseEntity.ok(updatedDeposit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeposit(@PathVariable Integer id) {
        if (!depositRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        depositRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

package com.bank_deposits.demo.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Deposit {

    @Id
    @SequenceGenerator(
            name = "deposit_id_sequence",
            sequenceName = "deposit_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "deposit_id_sequence"
    )

    private Integer id;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;


    private LocalDate openDate;
    private Double percent;
    private Integer timeMonth;

    public Deposit(Integer id,
                   Client client,
                   Bank bank,
                   LocalDate openDate,
                   Double percent,
                   Integer timeMonth) {
        this.id = id;
        this.client = client;
        this.bank = bank;
        this.openDate = openDate;
        this.percent = percent;
        this.timeMonth = timeMonth;
    }

    public Deposit() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Integer getTimeMonth() {
        return timeMonth;
    }

    public void setTimeMonth(Integer timeMonth) {
        this.timeMonth = timeMonth;
    }

}

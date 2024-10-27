package com.bank_deposits.demo.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Bank {
    @Id
    @SequenceGenerator(
            name = "bank_id_sequence",
            sequenceName = "bank_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "bank_id_sequence"
    )

    private int id;
    private String name;
    private String bic_number;

    public Bank(Integer id,
                String name,
                String bic_number) {
        this.id = id;
        this.name = name;
        this.bic_number = bic_number;
    }

    public Bank() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBic_number() {
        return bic_number;
    }

    public void setBic_number(String bic_number) {
        this.bic_number = bic_number;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Objects.equals(id, bank.id) && Objects.equals(name, bank.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Bank{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bic='" + bic_number +
                "}";
    }

}

package com.bank_deposits.demo.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Client {

    @Id
    @SequenceGenerator(
            name = "client_id_sequence",
            sequenceName = "client_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "client_id_sequence"
    )

    private Integer id;
    private String name;
    private String shortName;
    private String email;

    @Enumerated(EnumType.STRING)
    private LegalForm legalForm;

    public enum LegalForm {
        LLC, LP, JSC, Individual
    }

    public Client( Integer id,
                   String name,
                   String shortName,
                   String email,
                   LegalForm legalForm ) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.email = email;
        this.legalForm = legalForm;
    }

    public Client() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LegalForm getLegalForm() {
        return legalForm;
    }

    public void setLegalForm(LegalForm legalForm) {
        this.legalForm = legalForm;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && Objects.equals(name, client.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", email=" + email + '\'' +
                ", legalForm=" + legalForm +
                "}";
    }
}

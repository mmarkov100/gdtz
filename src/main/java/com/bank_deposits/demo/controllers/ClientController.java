package com.bank_deposits.demo.controllers;

import com.bank_deposits.demo.entities.Client;
import com.bank_deposits.demo.repos.ClientRepository;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientRepository clientRepository;

    public ClientController(final ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    record NewClient(String name,
                  String shortName,
                  String email,
                  Client.LegalForm legalForm) {}

    @GetMapping
    public List<Client> getClients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String shortName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Client.LegalForm legalForm,
            @RequestParam(required = false, defaultValue = "id") String sortBy) {

        Sort sort = Sort.by(sortBy);

        if (name != null || shortName != null || email != null || legalForm != null) {
            return clientRepository.findByNameContainingAndShortNameContainingAndEmailContainingAndLegalForm(
                    name != null ? name : "",
                    shortName != null ? shortName : "",
                    email != null ? email : "",
                    legalForm,
                    sort
            );
        }
        return clientRepository.findAll(sort);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Integer id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public void addClient(@RequestBody NewClient request) {
        Client client = new Client();
        client.setName(request.name);
        client.setShortName(request.shortName);
        client.setEmail(request.email);
        client.setLegalForm(request.legalForm);
        clientRepository.save(client);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Integer id, @RequestBody Client client) {
        if (!clientRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        client.setId(id);
        Client updatedClient = clientRepository.save(client);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer id) {
        if (!clientRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

package com.bank_deposits.demo.controllers;

import com.bank_deposits.demo.entities.Client;
import com.bank_deposits.demo.repos.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @InjectMocks
    private ClientController clientController;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetClients() {

        Client client1 = new Client(1, "John Doe", "JD", "john@example.com", Client.LegalForm.LLC);
        Client client2 = new Client(2, "Jane Smith", "JS", "jane@example.com", Client.LegalForm.Individual);
        List<Client> clients = new ArrayList<>();
        clients.add(client1);
        clients.add(client2);

        when(clientRepository.findAll(Sort.by("id"))).thenReturn(clients);

        List<Client> result = clientController.getClients(null, null, null, null, "id");

        assertEquals(2, result.size());
        verify(clientRepository, times(1)).findAll(Sort.by("id"));
    }

    @Test
    void testGetClientById_Found() {
        Client client = new Client(1, "John Doe", "JD", "john@example.com", Client.LegalForm.LLC);

        when(clientRepository.findById(1)).thenReturn(Optional.of(client));

        ResponseEntity<Client> result = clientController.getClientById(1);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(client, result.getBody());
        verify(clientRepository, times(1)).findById(1);
    }

    @Test
    void testGetClientById_NotFound() {
        when(clientRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Client> result = clientController.getClientById(1);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(clientRepository, times(1)).findById(1);
    }

    @Test
    void testAddClient() {
        ClientController.NewClient newClient = new ClientController.NewClient("John Doe", "JD", "john@example.com", Client.LegalForm.LLC);

        clientController.addClient(newClient);

        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void testUpdateClient_Found() {
        Client existingClient = new Client(1, "John Doe", "JD", "john@example.com", Client.LegalForm.LLC);
        Client updatedClient = new Client(1, "John Smith", "JS", "john.smith@example.com", Client.LegalForm.LLC);

        when(clientRepository.existsById(1)).thenReturn(true);
        when(clientRepository.save(any(Client.class))).thenReturn(updatedClient);

        ResponseEntity<Client> result = clientController.updateClient(1, updatedClient);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updatedClient, result.getBody());
        verify(clientRepository, times(1)).existsById(1);
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void testUpdateClient_NotFound() {
        Client updatedClient = new Client(1, "John Smith", "JS", "john.smith@example.com", Client.LegalForm.LLC);

        when(clientRepository.existsById(1)).thenReturn(false);

        ResponseEntity<Client> result = clientController.updateClient(1, updatedClient);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(clientRepository, times(1)).existsById(1);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void testDeleteClient_Found() {
        when(clientRepository.existsById(1)).thenReturn(true);

        ResponseEntity<Void> result = clientController.deleteClient(1);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(clientRepository, times(1)).existsById(1);
        verify(clientRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteClient_NotFound() {
        when(clientRepository.existsById(1)).thenReturn(false);

        ResponseEntity<Void> result = clientController.deleteClient(1);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(clientRepository, times(1)).existsById(1);
        verify(clientRepository, never()).deleteById(1);
    }
}

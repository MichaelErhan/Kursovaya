package com.example.cargo.service;

import com.example.cargo.model.BlogPost;
import com.example.cargo.model.Clients;
import com.example.cargo.repository.BlogPostRepository;
import com.example.cargo.repository.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientsService {

    private final ClientsRepository clientsRepository;

    @Autowired
    public ClientsService(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    public void saveClients(Clients clients) { clientsRepository.save(clients); }

    public Optional<Clients> findClientsById(Long id) {
        return clientsRepository.findById(id);
    }

    public List<Clients> findAllClients() {
        return clientsRepository.findAll();
    }

    public void deleteClients(Long id) {
        clientsRepository.deleteById(id);
    }

    public List<Clients> searchClients(String FIO, String BrandNumberAuto, String Number) {
        return clientsRepository.searchByQuery(FIO, BrandNumberAuto, Number);
    }
}

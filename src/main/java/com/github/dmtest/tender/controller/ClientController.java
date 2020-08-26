package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.domain.Client;
import com.github.dmtest.tender.repo.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("clients")
public class ClientController {
    private final ClientRepo clientRepo;

    @Autowired
    public ClientController(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    @GetMapping("getClients")
    public List<Client> getClients() {
        return clientRepo.findAll();
    }

    @PostMapping("addClient")
    public Client addClient(@RequestBody Client client) {
        return clientRepo.save(client);
    }
}

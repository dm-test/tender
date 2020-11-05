package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.dto.rq.client.AddClientRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("clients")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("getClients")
    public OperationResultRsDto getClients() {
        return clientService.getClients();
    }

    @PostMapping("addClient")
    public OperationResultRsDto addClient(@RequestBody AddClientRqDto addClientRqDto) {
        return clientService.addClient(addClientRqDto);
    }
}

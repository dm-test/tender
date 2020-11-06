package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.dto.rq.client.AddClientRqDto;
import com.github.dmtest.tender.dto.rq.client.RemoveClientRqDto;
import com.github.dmtest.tender.dto.rq.client.UpdateClientRqDto;
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

    @GetMapping("getClientDetails")
    public OperationResultRsDto getClientDetails(@RequestParam("clientName") String clientName) {
        return clientService.getClientDetails(clientName);
    }

    @PostMapping("addClient")
    public OperationResultRsDto addClient(@RequestBody AddClientRqDto addClientRqDto) {
        return clientService.addClient(addClientRqDto);
    }

    @PutMapping("updateClient")
    public OperationResultRsDto updateClient(@RequestBody UpdateClientRqDto updateClientRqDto) {
        return clientService.updateClient(updateClientRqDto);
    }

    @DeleteMapping("removeClient")
    public OperationResultRsDto removeClient(@RequestBody RemoveClientRqDto removeClientRqDto) {
        return clientService.removeClient(removeClientRqDto);
    }
}

package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.domain.Client;
import com.github.dmtest.tender.dto.rq.ClientRqDto;
import com.github.dmtest.tender.dto.rs.ClientRsDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.enums.OperationResult;
import com.github.dmtest.tender.repo.ClientsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("clients")
public class ClientController {
    private static final Logger LOG = LoggerFactory.getLogger(ClientController.class);
    private final ClientsRepo clientsRepo;

    @Autowired
    public ClientController(ClientsRepo clientsRepo) {
        this.clientsRepo = clientsRepo;
    }

    @GetMapping("getClients")
    public List<ClientRsDto> getClients() {
        List<ClientRsDto> clients = clientsRepo.findAll().stream()
                .map(client -> new ClientRsDto(client.getClientName()))
                .collect(Collectors.toList());
        LOG.info("Получен список клиентов");
        return clients;
    }

    @PostMapping("addClient")
    public OperationResultRsDto addClient(@RequestBody ClientRqDto clientRqDto) {
        String clientName = clientRqDto.getClientName();
        Client client = new Client(clientName);
        clientsRepo.save(client);
        String msg = String.format("Клиент с именем '%s' добавлен", clientName);
        LOG.info(msg);
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }
}

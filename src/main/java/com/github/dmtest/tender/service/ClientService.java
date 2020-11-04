package com.github.dmtest.tender.service;

import com.github.dmtest.tender.domain.Client;
import com.github.dmtest.tender.domain.Tender;
import com.github.dmtest.tender.enums.OperationResult;
import com.github.dmtest.tender.exception.BusinessException;
import com.github.dmtest.tender.repo.ClientsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private static final Logger LOG = LoggerFactory.getLogger(ClientService.class);
    private final ClientsRepo clientsRepo;

    @Autowired
    public ClientService(ClientsRepo clientsRepo) {
        this.clientsRepo = clientsRepo;
    }

    public Client getClientByClientName(String clientName) {
        return clientsRepo.findByClientName(clientName)
                .orElseThrow(() -> new BusinessException(
                        OperationResult.CLIENT_NOT_FOUND, String.format("Клиент с именем '%s' не найден", clientName)));
    }

    public void saveClient(Client client) {
        clientsRepo.save(client);
    }

    public Tender getClientTender(Client client, String tenderNumber) {
        return client.getTender(tenderNumber).orElseThrow(() -> new BusinessException(
                OperationResult.TENDER_NOT_FOUND, String.format(
                        "Тендер с номером '%s' у клиента '%s' не найден", tenderNumber, client.getClientName())));
    }

    public void removeClientTender(Client client, String tenderNumber) {
        boolean result = client.removeTender(tenderNumber);
        if (!result) {
            throw new BusinessException(OperationResult.TENDER_NOT_FOUND, String.format(
                    "Тендер с номером '%s' у клиента '%s' не найден", tenderNumber, client.getClientName()));
        }
    }


}

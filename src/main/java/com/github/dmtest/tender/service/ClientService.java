package com.github.dmtest.tender.service;

import com.github.dmtest.tender.domain.Client;
import com.github.dmtest.tender.domain.Contract;
import com.github.dmtest.tender.dto.rq.client.AddClientRqDto;
import com.github.dmtest.tender.dto.rq.client.RemoveClientRqDto;
import com.github.dmtest.tender.dto.rq.client.UpdateClientRqDto;
import com.github.dmtest.tender.dto.rq.contract.AddClientContractRqDto;
import com.github.dmtest.tender.dto.rq.contract.RemoveClientContractRqDto;
import com.github.dmtest.tender.dto.rq.contract.UpdateClientContractRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.dto.rs.body.client.GetClientDetailsRsDto;
import com.github.dmtest.tender.dto.rs.body.GetClientContractRsDto;
import com.github.dmtest.tender.dto.rs.body.client.GetClientRsDto;
import com.github.dmtest.tender.enums.OperationResult;
import com.github.dmtest.tender.exception.BusinessException;
import com.github.dmtest.tender.repo.ClientsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private static final Logger LOG = LoggerFactory.getLogger(ClientService.class);
    private final ClientsRepo clientsRepo;

    @Autowired
    public ClientService(ClientsRepo clientsRepo) {
        this.clientsRepo = clientsRepo;
    }

    public OperationResultRsDto getClients() {
        List<GetClientRsDto> getClientRsDtoList = clientsRepo.findAll().stream()
                .map(client -> new GetClientRsDto(client.getClientName()))
                .collect(Collectors.toList());
        LOG.info("Получен список клиентов");
        return new OperationResultRsDto(OperationResult.SUCCESS, getClientRsDtoList);
    }

    public OperationResultRsDto getClientDetails(String clientName) {
        Client client = getClientByClientName(clientName);
        String clientAddress = client.getClientAddress();
        GetClientDetailsRsDto getClientDetailsRsDto = new GetClientDetailsRsDto(clientName, clientAddress);
        LOG.info("Получена информация по клиенту '{}'", client);
        return new OperationResultRsDto(OperationResult.SUCCESS, getClientDetailsRsDto);
    }

    public OperationResultRsDto addClient(AddClientRqDto addClientRqDto) {
        String clientName = addClientRqDto.getClientName();
        String clientAddress = addClientRqDto.getClientAddress();
        Client client = new Client(clientName, clientAddress);
        clientsRepo.save(client);
        String msg = String.format("Клиент с именем '%s' добавлен", clientName);
        LOG.info(msg);
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }

    public OperationResultRsDto updateClient(UpdateClientRqDto updateClientRqDto) {
        String clientName = updateClientRqDto.getClientName();
        Client client = getClientByClientName(clientName);
        String clientAddress = client.getClientAddress();
        String clientNameNew = updateClientRqDto.getUpdatableData().getClientNameNew();
        String clientAddressNew = updateClientRqDto.getUpdatableData().getClientAddressNew();
        client.setClientName(clientNameNew);
        client.setClientAddress(clientAddressNew);
        clientsRepo.save(client);
        LOG.info("Клиент с именем '{}' обновлен. Имя клиента: '{}' -> '{}', Адрес клиента: '{}' -> '{}'",
                clientName, clientName, clientNameNew, clientAddress, clientAddressNew);
        return new OperationResultRsDto(OperationResult.SUCCESS, "Клиент успешно обновлен");
    }

    public OperationResultRsDto removeClient(RemoveClientRqDto removeClientRqDto) {
        String clientName = removeClientRqDto.getClientName();
        Client client = getClientByClientName(clientName);
        clientsRepo.delete(client);
        LOG.info("Клиент '{}' удален", client);
        return new OperationResultRsDto(OperationResult.SUCCESS, String.format("Клиент '%s' успешно удален", clientName));
    }

    public OperationResultRsDto getClientContracts(String clientName) {
        Client client = getClientByClientName(clientName);
        List<GetClientContractRsDto> getClientContractRsDtoList = client.getContracts().stream()
                .map(c -> new GetClientContractRsDto(c.getContractNumber(), c.getContractDate(), c.getClient().getClientName()))
                .collect(Collectors.toList());
        LOG.info("Получен список контрактов с клиентом '{}'", client);
        return new OperationResultRsDto(OperationResult.SUCCESS, getClientContractRsDtoList);
    }

    public OperationResultRsDto getClientContractDetails(String clientName, String contractNumber) {
        Client client = getClientByClientName(clientName);
        Contract contract = getClientContractByContractNumber(client, contractNumber);
        LocalDate contractDate = contract.getContractDate();
        GetClientContractRsDto getClientContractRsDto = new GetClientContractRsDto(contractNumber, contractDate, clientName);
        LOG.info("Получена информация по контракту '{}'", contract);
        return new OperationResultRsDto(OperationResult.SUCCESS, getClientContractRsDto);
    }

    public OperationResultRsDto addClientContract(AddClientContractRqDto addClientContractRqDto) {
        String clientName = addClientContractRqDto.getClientName();
        String contractNumber = addClientContractRqDto.getContractNumber();
        LocalDate contractDate = addClientContractRqDto.getContractDate();
        Client client = getClientByClientName(clientName);
        Contract contract = new Contract(contractNumber, contractDate, client);
        client.addContract(contract);
        clientsRepo.save(client);
        LOG.info("Контракт '{}' добавлен клиенту '{}'", contract, client);
        String msg = String.format("Контракт с номером '%s' добавлен клиенту '%s'", contractNumber, clientName);
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }

    public OperationResultRsDto updateClientContract(UpdateClientContractRqDto updateClientContractRqDto) {
        String clientName = updateClientContractRqDto.getClientName();
        String contractNumber = updateClientContractRqDto.getContractNumber();
        Client client = getClientByClientName(clientName);
        Contract contract = getClientContractByContractNumber(client, contractNumber);
        LocalDate contractDate = contract.getContractDate();
        String contractNumberNew = updateClientContractRqDto.getUpdatableData().getContractNumberNew();
        LocalDate contractDateNew = updateClientContractRqDto.getUpdatableData().getContractDateNew();
        contract.setContractNumber(contractNumberNew);
        contract.setContractDate(contractDateNew);
        clientsRepo.save(client);
        LOG.info("Контракт с номером '{}' обновлен. Номер контракта: '{}' -> '{}', Дата контракта: '{}' -> '{}'",
                contractNumber, contractNumber, contractNumberNew, contractDate, contractDateNew);
        return new OperationResultRsDto(OperationResult.SUCCESS, "Контракт успешно обновлен");
    }

    public OperationResultRsDto removeClientContract(RemoveClientContractRqDto removeClientContractRqDto) {
        String clientName = removeClientContractRqDto.getClientName();
        String contractNumber = removeClientContractRqDto.getContractNumber();
        Client client = getClientByClientName(clientName);
        Contract contract = getClientContractByContractNumber(client, contractNumber);
        client.removeContract(contract);
        clientsRepo.save(client);
        LOG.info("Контракт '{}' удален у клиента '{}'", contract, client);
        return new OperationResultRsDto(OperationResult.SUCCESS, String.format("Контракт '%s' успешно удален", contractNumber));
    }

    private Client getClientByClientName(String clientName) {
        return clientsRepo.findByClientName(clientName)
                .orElseThrow(() -> new BusinessException(
                        OperationResult.CLIENT_NOT_FOUND, String.format("Клиент с именем '%s' не найден", clientName)));
    }

    private Contract getClientContractByContractNumber(Client client, String contractNumber) {
        return client.getContract(contractNumber).orElseThrow(() -> new BusinessException(
                OperationResult.CONTRACT_NOT_FOUND, String.format(
                        "Контракт с номером '%s' у клиента '%s' не найден", contractNumber, client.getClientName())));
    }

}

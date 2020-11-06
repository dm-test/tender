package com.github.dmtest.tender.service;

import com.github.dmtest.tender.domain.Client;
import com.github.dmtest.tender.domain.Tender;
import com.github.dmtest.tender.dto.rq.client.AddClientRqDto;
import com.github.dmtest.tender.dto.rq.client.RemoveClientRqDto;
import com.github.dmtest.tender.dto.rq.client.UpdateClientRqDto;
import com.github.dmtest.tender.dto.rq.tender.AddTenderRqDto;
import com.github.dmtest.tender.dto.rq.tender.RemoveTenderRqDto;
import com.github.dmtest.tender.dto.rq.tender.UpdateTenderRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.dto.rs.body.GetClientRsDto;
import com.github.dmtest.tender.dto.rs.body.GetTenderRsDto;
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
        List<GetClientRsDto> clients = clientsRepo.findAll().stream()
                .map(client -> new GetClientRsDto(client.getClientName()))
                .collect(Collectors.toList());
        LOG.info("Получен список клиентов");
        return new OperationResultRsDto(OperationResult.SUCCESS, clients);
    }

    public OperationResultRsDto addClient(AddClientRqDto addClientRqDto) {
        String clientName = addClientRqDto.getClientName();
        Client client = new Client(clientName);
        clientsRepo.save(client);
        String msg = String.format("Клиент с именем '%s' добавлен", clientName);
        LOG.info(msg);
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }

    public OperationResultRsDto updateClient(UpdateClientRqDto updateClientRqDto) {
        String clientName = updateClientRqDto.getClientName();
        Client client = getClientByClientName(clientName);
        String clientNameNew = updateClientRqDto.getUpdatableData().getClientNameNew();
        client.setClientName(clientNameNew);
        clientsRepo.save(client);
        LOG.info("Клиент с именем '{}' обновлен. Имя клиента: '{}' -> '{}'", clientName, clientName, clientNameNew);
        return new OperationResultRsDto(OperationResult.SUCCESS, "Клиент успешно обновлен");
    }

    public OperationResultRsDto removeClient(RemoveClientRqDto removeClientRqDto) {
        String clientName = removeClientRqDto.getClientName();
        Client client = getClientByClientName(clientName);
        clientsRepo.delete(client);
        LOG.info("Клиент '{}' удален", client);
        return new OperationResultRsDto(OperationResult.SUCCESS, String.format("Клиент '%s' успешно удален", clientName));
    }

    public OperationResultRsDto getClientTenders(String clientName) {
        Client client = getClientByClientName(clientName);
        List<GetTenderRsDto> getTenderRsDtoList = client.getTenders().stream()
                .map(tn -> new GetTenderRsDto(tn.getTenderNumber(), tn.getTenderDate(), tn.getClient().getClientName()))
                .collect(Collectors.toList());
        LOG.info("Получен список тендеров клиента '{}'", client);
        return new OperationResultRsDto(OperationResult.SUCCESS, getTenderRsDtoList);
    }

    public OperationResultRsDto getClientTender(String clientName, String tenderNumber) {
        Client client = getClientByClientName(clientName);
        Tender tender = getClientTenderByTenderNumber(client, tenderNumber);
        LocalDate tenderDate = tender.getTenderDate();
        GetTenderRsDto getTenderRsDto = new GetTenderRsDto(tenderNumber, tenderDate, clientName);
        LOG.info("Получена информация по тендеру '{}'", tender);
        return new OperationResultRsDto(OperationResult.SUCCESS, getTenderRsDto);
    }

    public OperationResultRsDto addClientTender(AddTenderRqDto addTenderRqDto) {
        String clientName = addTenderRqDto.getClientName();
        String tenderNumber = addTenderRqDto.getTenderNumber();
        LocalDate tenderDate = addTenderRqDto.getTenderDate();
        Client client = getClientByClientName(clientName);
        Tender tender = new Tender(tenderNumber, tenderDate, client);
        client.addTender(tender);
        clientsRepo.save(client);
        LOG.info("Тендер '{}' добавлен клиенту '{}'", tender, client);
        String msg = String.format("Тендер с номером '%s' добавлен клиенту '%s'", tenderNumber, clientName);
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }

    public OperationResultRsDto updateClientTender(UpdateTenderRqDto updateTenderRqDto) {
        String clientName = updateTenderRqDto.getClientName();
        String tenderNumber = updateTenderRqDto.getTenderNumber();
        Client client = getClientByClientName(clientName);
        Tender tender = getClientTenderByTenderNumber(client, tenderNumber);
        LocalDate tenderDate = tender.getTenderDate();
        String tenderNumberNew = updateTenderRqDto.getUpdatableData().getTenderNumberNew();
        LocalDate tenderDateNew = updateTenderRqDto.getUpdatableData().getTenderDateNew();
        tender.setTenderNumber(tenderNumberNew);
        tender.setTenderDate(tenderDateNew);
        clientsRepo.save(client);
        LOG.info("Тендер с номером '{}' обновлен. Номер тендера: '{}' -> '{}', Дата тендера: '{}' -> '{}'",
                tenderNumber, tenderNumber, tenderNumberNew, tenderDate, tenderDateNew);
        return new OperationResultRsDto(OperationResult.SUCCESS, "Тендер успешно обновлен");
    }

    public OperationResultRsDto removeClientTender(RemoveTenderRqDto removeTenderRqDto) {
        String clientName = removeTenderRqDto.getClientName();
        String tenderNumber = removeTenderRqDto.getTenderNumber();
        Client client = getClientByClientName(clientName);
        Tender tender = getClientTenderByTenderNumber(client, tenderNumber);
        client.removeTender(tender);
        clientsRepo.save(client);
        LOG.info("Тендер '{}' удален у клиента '{}'", tender, client);
        return new OperationResultRsDto(OperationResult.SUCCESS, String.format("Тендер '%s' успешно удален", tenderNumber));
    }

    private Client getClientByClientName(String clientName) {
        return clientsRepo.findByClientName(clientName)
                .orElseThrow(() -> new BusinessException(
                        OperationResult.CLIENT_NOT_FOUND, String.format("Клиент с именем '%s' не найден", clientName)));
    }

    private Tender getClientTenderByTenderNumber(Client client, String tenderNumber) {
        return client.getTender(tenderNumber).orElseThrow(() -> new BusinessException(
                OperationResult.TENDER_NOT_FOUND, String.format(
                        "Тендер с номером '%s' у клиента '%s' не найден", tenderNumber, client.getClientName())));
    }

}

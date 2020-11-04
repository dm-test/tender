package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.domain.Client;
import com.github.dmtest.tender.domain.Tender;
import com.github.dmtest.tender.dto.rq.tender.AddTenderRqDto;
import com.github.dmtest.tender.dto.rq.tender.GetTendersRqDto;
import com.github.dmtest.tender.dto.rq.tender.RemoveTenderRqDto;
import com.github.dmtest.tender.dto.rq.tender.UpdateTenderRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.dto.rs.body.GetTenderRsDto;
import com.github.dmtest.tender.enums.OperationResult;
import com.github.dmtest.tender.service.ClientService;
import com.github.dmtest.tender.service.TenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("tenders")
public class TenderController {
    private static final Logger LOG = LoggerFactory.getLogger(TenderController.class);
    private final ClientService clientService;
    private final TenderService tenderService;

    @Autowired
    public TenderController(ClientService clientService, TenderService tenderService) {
        this.clientService = clientService;
        this.tenderService = tenderService;
    }

    @PostMapping("getTenders")
    public OperationResultRsDto getTenders(@RequestBody GetTendersRqDto tendersRqDto) {
        String clientName = tendersRqDto.getClientName();
        List<Tender> tenders;
        if (clientName == null) {
            tenders = tenderService.getAllTenders();
        } else {
            Client client = clientService.getClientByClientName(clientName);
            tenders = client.getTenders();
        }
        List<GetTenderRsDto> getTenderRsDtoList = tenders.stream()
                .map(tn -> new GetTenderRsDto(tn.getTenderNumber(), tn.getTenderDate(), tn.getClient().getClientName()))
                .collect(Collectors.toList());
        LOG.info("Получен список тендеров клиента '{}'", clientName);
        return new OperationResultRsDto(OperationResult.SUCCESS, getTenderRsDtoList);
    }

    @GetMapping("getTender")
    public OperationResultRsDto getTender(@RequestParam("tenderNumber") String tenderNumber) {
        Tender tender = tenderService.getTenderByTenderNumber(tenderNumber);
        LocalDate tenderDate = tender.getTenderDate();
        String clientName = tender.getClient().getClientName();
        GetTenderRsDto getTenderRsDto = new GetTenderRsDto(tenderNumber, tenderDate, clientName);
        LOG.info("Получена информация по тендеру '{}'", tender);
        return new OperationResultRsDto(OperationResult.SUCCESS, getTenderRsDto);
    }

    @PostMapping("addTender")
    public OperationResultRsDto addTender(@RequestBody AddTenderRqDto addTenderRqDto) {
        String clientName = addTenderRqDto.getClientName();
        String tenderNumber = addTenderRqDto.getTenderNumber();
        LocalDate tenderDate = addTenderRqDto.getTenderDate();
        Client client = clientService.getClientByClientName(clientName);
        Tender tender = new Tender(tenderNumber, tenderDate, client);
        client.addTender(tender);
        clientService.saveClient(client);
        LOG.info("Тендер '{}' добавлен клиенту '{}'", tender, client);
        String msg = String.format("Тендер с номером '%s' добавлен клиенту '%s'", tenderNumber, clientName);
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }

    @PutMapping("updateTender")
    public OperationResultRsDto updateTender(@RequestBody UpdateTenderRqDto updateTenderRqDto) {
        String clientName = updateTenderRqDto.getClientName();
        String tenderNumber = updateTenderRqDto.getTenderNumber();
        Client client = clientService.getClientByClientName(clientName);
        Tender tender = clientService.getClientTender(client, tenderNumber);
        LocalDate tenderDate = tender.getTenderDate();
        String tenderNumberNew = updateTenderRqDto.getUpdatableData().getTenderNumberNew();
        LocalDate tenderDateNew = updateTenderRqDto.getUpdatableData().getTenderDateNew();
        tender.setTenderNumber(tenderNumberNew);
        tender.setTenderDate(tenderDateNew);
        clientService.saveClient(client);
        LOG.info("Тендер с номером '{}' обновлен. Номер тендера: '{}' -> '{}', Дата тендера: '{}' -> '{}'", tenderNumber, tenderNumber, tenderNumberNew, tenderDate, tenderDateNew);
        return new OperationResultRsDto(OperationResult.SUCCESS, "Тендер успешно обновлен");
    }

    @DeleteMapping("removeTender")
    public OperationResultRsDto removeTender(@RequestBody RemoveTenderRqDto removeTenderRqDto) {
        String clientName = removeTenderRqDto.getClientName();
        String tenderNumber = removeTenderRqDto.getTenderNumber();
        Client client = clientService.getClientByClientName(clientName);
        clientService.removeClientTender(client, tenderNumber);
        clientService.saveClient(client);
        LOG.info("Тендер с номером '{}' удален у клиента '{}'", tenderNumber, clientName);
        return new OperationResultRsDto(OperationResult.SUCCESS, String.format("Тендер '%s' успешно удален", tenderNumber));
    }

}

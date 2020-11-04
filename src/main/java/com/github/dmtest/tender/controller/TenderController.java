package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.domain.Client;
import com.github.dmtest.tender.domain.Tender;
import com.github.dmtest.tender.dto.rq.tender.AddTenderRqDto;
import com.github.dmtest.tender.dto.rq.tender.RemoveTenderRqDto;
import com.github.dmtest.tender.dto.rq.tender.GetTendersRqDto;
import com.github.dmtest.tender.dto.rq.tender.UpdateTenderRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.dto.rs.body.GetTenderRsDto;
import com.github.dmtest.tender.enums.OperationResult;
import com.github.dmtest.tender.exception.BusinessException;
import com.github.dmtest.tender.repo.ClientsRepo;
import com.github.dmtest.tender.repo.TendersRepo;
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
    private final ClientsRepo clientsRepo;
    private final TendersRepo tendersRepo;

    @Autowired
    public TenderController(ClientsRepo clientsRepo, TendersRepo tendersRepo) {
        this.clientsRepo = clientsRepo;
        this.tendersRepo = tendersRepo;
    }

    @PostMapping("getTenders")
    public OperationResultRsDto getTenders(@RequestBody GetTendersRqDto tendersRqDto) {
        String clientName = tendersRqDto.getClientName();
        List<Tender> tenders;
        if (clientName == null) {
            tenders = tendersRepo.findAll();
        } else {
            Client client = clientsRepo.findByClientName(clientName)
                    .orElseThrow(() -> new BusinessException(OperationResult.CLIENT_NOT_FOUND, String.format("Клиент с именем '%s' не найден", clientName)));
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
        Tender tender = tendersRepo.findByTenderNumber(tenderNumber)
                .orElseThrow(() -> new BusinessException(OperationResult.TENDER_NOT_FOUND, String.format("Тендер с номером '%s' не найден", tenderNumber)));
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
        Client client = clientsRepo.findByClientName(clientName)
                .orElseThrow(() -> new BusinessException(OperationResult.CLIENT_NOT_FOUND, String.format("Клиент с именем '%s' не найден", clientName)));
        Tender tender = new Tender(tenderNumber, tenderDate, client);
        client.addTender(tender);
        clientsRepo.save(client);
        LOG.info("Тендер '{}' добавлен клиенту '{}'", tender, client);
        String msg = String.format("Тендер с номером '%s' добавлен клиенту '%s'", tenderNumber, clientName);
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }

    @PutMapping("updateTender")
    public OperationResultRsDto updateTender(@RequestBody UpdateTenderRqDto updateTenderRqDto) {
        String clientName = updateTenderRqDto.getClientName();
        String tenderNumber = updateTenderRqDto.getTenderNumber();
        Client client = clientsRepo.findByClientName(clientName)
                .orElseThrow(() -> new BusinessException(OperationResult.CLIENT_NOT_FOUND, String.format("Клиент с именем '%s' не найден", clientName)));
        Tender tender = client.getTender(tenderNumber)
                .orElseThrow(() -> new BusinessException(OperationResult.TENDER_NOT_FOUND, String.format("Тендер с номером '%s' у клиента '%s' не найден", tenderNumber, clientName)));
        LocalDate tenderDate = tender.getTenderDate();
        String tenderNumberNew = updateTenderRqDto.getUpdatableData().getTenderNumberNew();
        LocalDate tenderDateNew = updateTenderRqDto.getUpdatableData().getTenderDateNew();
        tender.setTenderNumber(tenderNumberNew);
        tender.setTenderDate(tenderDateNew);
        clientsRepo.save(client);
        LOG.info("Тендер с номером '{}' обновлен. Номер тендера: '{}' -> '{}', Дата тендера: '{}' -> '{}'", tenderNumber, tenderNumber, tenderNumberNew, tenderDate, tenderDateNew);
        return new OperationResultRsDto(OperationResult.SUCCESS, "Тендер успешно обновлен");
    }

    @DeleteMapping("removeTender")
    public OperationResultRsDto removeTender(@RequestBody RemoveTenderRqDto removeTenderRqDto) {
        String clientName = removeTenderRqDto.getClientName();
        String tenderNumber = removeTenderRqDto.getTenderNumber();
        Client client = clientsRepo.findByClientName(clientName)
                .orElseThrow(() -> new BusinessException(OperationResult.CLIENT_NOT_FOUND, String.format("Клиент с именем '%s' не найден", clientName)));
        boolean result = client.removeTender(tenderNumber);
        if (!result) {
            throw new BusinessException(OperationResult.TENDER_NOT_FOUND, String.format("Тендер с номером '%s' у клиента '%s' не найден", tenderNumber, clientName));
        }
        clientsRepo.save(client);
        LOG.info("Тендер с номером '{}' удален у клиента '{}'", tenderNumber, clientName);
        return new OperationResultRsDto(OperationResult.SUCCESS, String.format("Тендер '%s' успешно удален", tenderNumber));
    }

}

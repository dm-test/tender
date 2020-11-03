package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.domain.Client;
import com.github.dmtest.tender.domain.Tender;
import com.github.dmtest.tender.dto.rq.tender.AddTenderRqDto;
import com.github.dmtest.tender.dto.rq.tender.DeleteTenderRqDto;
import com.github.dmtest.tender.dto.rq.tender.GetTendersByClientIdRqDto;
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
import java.util.UUID;
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

    @PostMapping("getTendersByClientId")
    public OperationResultRsDto getTendersByClientId(@RequestBody GetTendersByClientIdRqDto getTendersByClientIdRqDto) {
        UUID clientId = getTendersByClientIdRqDto.getClientId();
        List<Tender> tenders;
        if (clientId == null) {
            tenders = tendersRepo.findAll();
        } else {
            Client client = clientsRepo.findById(clientId)
                    .orElseThrow(() -> new BusinessException(OperationResult.CLIENT_NOT_FOUND, String.format("Клиент с id '%s' не найден", clientId)));
            tenders = client.getTenders();
        }
        List<GetTenderRsDto> getTenderRsDtoList = tenders.stream()
                .map(tn -> new GetTenderRsDto(tn.getTenderId(), tn.getTenderNumber(), tn.getTenderDate(), tn.getClient().getClientName()))
                .collect(Collectors.toList());
        LOG.info("Получен список тендеров клиента с clientId '{}'", clientId);
        return new OperationResultRsDto(OperationResult.SUCCESS, getTenderRsDtoList);
    }

    @GetMapping("getTender")
    public OperationResultRsDto getTender(@RequestParam("tenderId") UUID tenderId) {
        Tender tender = tendersRepo.findById(tenderId)
                .orElseThrow(() -> new BusinessException(OperationResult.TENDER_NOT_FOUND, String.format("Тендер с id '%s' не найден", tenderId)));
        String tenderNumber = tender.getTenderNumber();
        LocalDate tenderDate = tender.getTenderDate();
        String clientName = tender.getClient().getClientName();
        GetTenderRsDto getTenderRsDto = new GetTenderRsDto(tenderId, tenderNumber, tenderDate, clientName);
        LOG.info("Получена информация по тендеру '{}'", tender);
        return new OperationResultRsDto(OperationResult.SUCCESS, getTenderRsDto);
    }

    @PostMapping("addTender")
    public OperationResultRsDto addTender(@RequestBody AddTenderRqDto addTenderRqDto) {
        UUID clientId = addTenderRqDto.getClientId();
        String tenderNumber = addTenderRqDto.getTenderNumber();
        LocalDate tenderDate = addTenderRqDto.getTenderDate();
        Tender tender = new Tender(tenderNumber, tenderDate);
        Client client = clientsRepo.findById(clientId)
                .orElseThrow(() -> new BusinessException(OperationResult.CLIENT_NOT_FOUND, String.format("Клиент с id '%s' не найден", clientId)));
        client.addTender(tender);
        clientsRepo.save(client);
        LOG.info("Тендер добавлен: {}", tender);
        String msg = String.format("Тендер с номером '%s' добавлен клиенту '%s'", tenderNumber, client.getClientName());
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }

    @PutMapping("updateTender")
    public OperationResultRsDto updateTender(@RequestBody UpdateTenderRqDto updateTenderRqDto) {
        UUID tenderId = updateTenderRqDto.getTenderId();
        UUID clientId = updateTenderRqDto.getClientId();
        LocalDate tenderDate = updateTenderRqDto.getTenderDate();
        Tender tender = tendersRepo.findById(tenderId)
                .orElseThrow(() -> new BusinessException(OperationResult.TENDER_NOT_FOUND, String.format("Тендер с id '%s' не найден", tenderId)));
        Client client = clientsRepo.findById(clientId)
                .orElseThrow(() -> new BusinessException(OperationResult.CLIENT_NOT_FOUND, String.format("Клиент с id '%s' не найден", clientId)));
        tender.setClient(client);
        tender.setTenderDate(tenderDate);
        tendersRepo.save(tender);
        LOG.info("Тендер обновлен: {}", tender);
        return new OperationResultRsDto(OperationResult.SUCCESS, String.format("Тендер '%s' успешно обновлен", tender.getTenderNumber()));
    }

    @DeleteMapping("deleteTender")
    public OperationResultRsDto deleteTender(@RequestBody DeleteTenderRqDto deleteTenderRqDto) {
        UUID tenderId = deleteTenderRqDto.getTenderId();
        Tender tender = tendersRepo.findById(tenderId)
                .orElseThrow(() -> new BusinessException(OperationResult.TENDER_NOT_FOUND, String.format("Тендер с id '%s' не найден", tenderId)));
        tendersRepo.delete(tender);
        LOG.info("Тендер удален: {}", tender);
        return new OperationResultRsDto(OperationResult.SUCCESS, String.format("Тендер '%s' успешно удален", tender.getTenderNumber()));
    }

}

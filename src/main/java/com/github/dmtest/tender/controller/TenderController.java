package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.domain.Client;
import com.github.dmtest.tender.domain.Tender;
import com.github.dmtest.tender.dto.rq.tenders.AddTenderRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.dto.rs.body.TenderRsDto;
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
    private final TendersRepo tendersRepo;
    private final ClientsRepo clientsRepo;

    @Autowired
    public TenderController(TendersRepo tendersRepo, ClientsRepo clientsRepo) {
        this.tendersRepo = tendersRepo;
        this.clientsRepo = clientsRepo;
    }

    @GetMapping("getTenders")
    public OperationResultRsDto getTenders() {
        List<TenderRsDto> tenders = tendersRepo.findAll().stream()
                .map(tender -> new TenderRsDto(tender.getTenderNumber(), tender.getTenderDate()))
                .collect(Collectors.toList());
        LOG.info("Получен список тендеров");
        return new OperationResultRsDto(OperationResult.SUCCESS, tenders);
    }

    @PostMapping("addTender")
    public OperationResultRsDto addTender(@RequestBody AddTenderRqDto addTenderRqDto) {
        String tenderNumber = addTenderRqDto.getTenderNumber();
        LocalDate tenderDate = addTenderRqDto.getTenderDate();
        String clientName = addTenderRqDto.getClientName();
        Client client = clientsRepo.findAll().stream()
                .filter(cl -> cl.getClientName().equals(clientName))
                .findFirst()
                .orElseThrow(() -> new BusinessException(OperationResult.CLIENT_NOT_FOUND, String.format("Клиент с именем '%s' не найден", clientName)));
        Tender tender = new Tender(tenderNumber, tenderDate, client);
        tendersRepo.save(tender);
        String msg = String.format("Тендер с номером '%s' добавлен клиенту '%s'", tenderNumber, clientName);
        LOG.info(msg);
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }

}

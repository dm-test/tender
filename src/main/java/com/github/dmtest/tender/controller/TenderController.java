package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.domain.Client;
import com.github.dmtest.tender.domain.Tender;
import com.github.dmtest.tender.dto.rq.TenderRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.enums.OperationResult;
import com.github.dmtest.tender.exception.BusinessException;
import com.github.dmtest.tender.repo.ClientsRepo;
import com.github.dmtest.tender.repo.TendersRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Tender> getTenders() {
        List<Tender> tenders = tendersRepo.findAll();
        LOG.info("Получен список тендеров");
        return tenders;
    }

    @PostMapping("addTender")
    public OperationResultRsDto addTender(@RequestBody TenderRqDto tenderRqDto) {
        String tenderNumber = tenderRqDto.getTenderNumber();
        String clientName = tenderRqDto.getClientName();
        Client client = clientsRepo.findAll().stream()
                .filter(cl -> cl.getClientName().equals(clientName))
                .findFirst()
                .orElseThrow(() -> new BusinessException(OperationResult.CLIENT_NOT_FOUND, String.format("Клиент с именем '%s' не найден", clientName)));
        Tender tender = new Tender(tenderNumber, client);
        tendersRepo.save(tender);
        String msg = String.format("Тендер с номером '%s' добавлен клиенту '%s'", tenderNumber, clientName);
        LOG.info(msg);
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }

}

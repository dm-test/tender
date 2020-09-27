package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.domain.Client;
import com.github.dmtest.tender.domain.Product;
import com.github.dmtest.tender.domain.Tender;
import com.github.dmtest.tender.domain.TenderItem;
import com.github.dmtest.tender.dto.rq.tenders.AddTenderRqDto;
import com.github.dmtest.tender.dto.rq.tenders.contents.TenderContentRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.dto.rs.body.TenderRsDto;
import com.github.dmtest.tender.enums.OperationResult;
import com.github.dmtest.tender.exception.BusinessException;
import com.github.dmtest.tender.repo.ClientsRepo;
import com.github.dmtest.tender.repo.ProductsRepo;
import com.github.dmtest.tender.repo.TendersRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("tenders")
public class TenderController {
    private static final Logger LOG = LoggerFactory.getLogger(TenderController.class);
    private final TendersRepo tendersRepo;
    private final ClientsRepo clientsRepo;
    private final ProductsRepo productsRepo;

    @Autowired
    public TenderController(TendersRepo tendersRepo, ClientsRepo clientsRepo, ProductsRepo productsRepo) {
        this.tendersRepo = tendersRepo;
        this.clientsRepo = clientsRepo;
        this.productsRepo = productsRepo;
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
        Set<TenderContentRqDto> tenderContents = addTenderRqDto.getTenderContents();
        Client client = clientsRepo.findAll().stream()
                .filter(cl -> cl.getClientName().equals(clientName))
                .findFirst()
                .orElseThrow(() -> new BusinessException(OperationResult.CLIENT_NOT_FOUND, String.format("Клиент с именем '%s' не найден", clientName)));
        Tender tender = new Tender(tenderNumber, tenderDate, client);
        tendersRepo.save(tender);
        tenderContents.forEach(content -> {
            String productName = content.getProductName();
            Integer quantity = content.getQuantity();
            BigDecimal costPerUnit = content.getCostPerUnit();
            Product product = productsRepo.findAll().stream()
                    .filter(pr -> pr.getProductName().equals(productName))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(OperationResult.TENDER_NOT_FOUND, String.format("Продукт с именем '%s' не найден", productName)));
            TenderItem item = new TenderItem(product, quantity, costPerUnit);
            tender.addItem(item);
        });
        String msg = String.format("Тендер с номером '%s' добавлен клиенту '%s'", tenderNumber, clientName);
        LOG.info(msg);
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }

}

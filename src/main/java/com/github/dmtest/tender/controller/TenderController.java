package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.domain.Client;
import com.github.dmtest.tender.domain.Product;
import com.github.dmtest.tender.domain.Tender;
import com.github.dmtest.tender.domain.TenderItem;
import com.github.dmtest.tender.dto.rq.AddTenderRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.dto.rs.body.TenderRsDto;
import com.github.dmtest.tender.enums.OperationResult;
import com.github.dmtest.tender.exception.BusinessException;
import com.github.dmtest.tender.repo.ClientsRepo;
import com.github.dmtest.tender.repo.ProductsRepo;
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
    private final ProductsRepo productsRepo;

    @Autowired
    public TenderController(ClientsRepo clientsRepo, ProductsRepo productsRepo) {
        this.clientsRepo = clientsRepo;
        this.productsRepo = productsRepo;
    }

    @GetMapping("getTenders")
    public OperationResultRsDto getTenders(@RequestParam("clientId") UUID clientId) {
        Client client = clientsRepo.findById(clientId)
                .orElseThrow(() -> new BusinessException(OperationResult.CLIENT_NOT_FOUND, String.format("Клиент с id '%s' не найден", clientId)));
        List<TenderRsDto> tenders = client.getTenders().stream()
                .map(tender -> new TenderRsDto(tender.getTenderNumber(), tender.getTenderDate()))
                .collect(Collectors.toList());
        LOG.info("Получен список тендеров клиента '{}'", clientId);
        return new OperationResultRsDto(OperationResult.SUCCESS, tenders);
    }

    @PostMapping("addTender")
    public OperationResultRsDto addTender(@RequestBody AddTenderRqDto addTenderRqDto) {
        String tenderNumber = addTenderRqDto.getTenderNumber();
        LocalDate tenderDate = addTenderRqDto.getTenderDate();
        Tender tender = new Tender(tenderNumber, tenderDate);
        addTenderRqDto.getTenderItems().forEach(dto -> {
            UUID productId = dto.getProductId();
            Product product = productsRepo.findById(productId)
                    .orElseThrow(() -> new BusinessException(OperationResult.PRODUCT_NOT_FOUND, String.format("Продукт с id '%s' не найден", productId)));
            TenderItem tenderItem = new TenderItem(tender, product, dto.getQuantity(), dto.getCostPerUnit());
            tender.addItem(tenderItem);
        });
        UUID clientId = addTenderRqDto.getClientId();
        Client client = clientsRepo.findById(clientId)
                .orElseThrow(() -> new BusinessException(OperationResult.CLIENT_NOT_FOUND, String.format("Клиент с id '%s' не найден", clientId)));
        client.addTender(tender);
        clientsRepo.save(client);
        String msg = String.format("Тендер с номером '%s' добавлен клиенту '%s'", tenderNumber, clientId);
        LOG.info(msg);
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }

}

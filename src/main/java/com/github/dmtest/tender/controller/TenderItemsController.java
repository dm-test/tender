package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.repo.TendersRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tenderItems")
public class TenderItemsController {
    private static final Logger LOG = LoggerFactory.getLogger(TenderItemsController.class);
    private final TendersRepo tendersRepo;

    @Autowired
    public TenderItemsController(TendersRepo tendersRepo) {
        this.tendersRepo = tendersRepo;
    }

//    @PostMapping("addTenderContents")
//    public OperationResultRsDto addTenderContents(@RequestBody AddTenderContentsRqDto addTenderContentsRqDto) {
//        String tenderNumber = addTenderContentsRqDto.getTenderNumber();
//        List<TenderContentRqDto> tenderContents = addTenderContentsRqDto.getTenderContents();
//        Tender tender = tendersRepo.findAll().stream()
//                .filter(td -> td.getTenderNumber().equals(tenderNumber))
//                .findFirst()
//                .orElseThrow(() -> new BusinessException(OperationResult.TENDER_NOT_FOUND, String.format("Тендер с номером '%s' не найден", tenderNumber)));
//        tenderContents.forEach(content -> {
//            String productName = content.getProductName();
//            Product product = productsRepo.findAll().stream()
//                    .filter(pr -> pr.getProductName().equals(productName))
//                    .findFirst()
//                    .orElseThrow(() -> new BusinessException(OperationResult.TENDER_NOT_FOUND, String.format("Продукт с именем '%s' не найден", productName)));
//            TenderItem tenderContent = new TenderItem(tender, product, content.getQuantity(), content.getCostPerUnit());
//            tenderContentsRepo.save(tenderContent);
//            LOG.info("Продукт {} добавлен тендеру {}", productName, tenderNumber);
//        });
//        String msg = String.format("Добавлены продукты тендеру с номером '%s'", tenderNumber);
//        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
//    }

}

package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.domain.Product;
import com.github.dmtest.tender.domain.Tender;
import com.github.dmtest.tender.domain.TenderItem;
import com.github.dmtest.tender.dto.rq.item.AddTenderItemRqDto;
import com.github.dmtest.tender.dto.rq.item.RemoveTenderItemRqDto;
import com.github.dmtest.tender.dto.rq.item.UpdateTenderItemRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.dto.rs.body.GetTenderItemRsDto;
import com.github.dmtest.tender.enums.OperationResult;
import com.github.dmtest.tender.service.ProductService;
import com.github.dmtest.tender.service.TenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("tenderItems")
public class TenderItemController {
    private static final Logger LOG = LoggerFactory.getLogger(TenderItemController.class);
    private final TenderService tenderService;
    private final ProductService productService;

    @Autowired
    public TenderItemController(TenderService tenderService, ProductService productService) {
        this.tenderService = tenderService;
        this.productService = productService;
    }

    @GetMapping("getTenderItems")
    public OperationResultRsDto getTenderItems(@RequestParam("tenderNumber") String tenderNumber) {
        Tender tender = tenderService.getTenderByTenderNumber(tenderNumber);
        List<GetTenderItemRsDto> getTenderItemRsDtoList = tender.getItems().stream()
                .map(it -> new GetTenderItemRsDto(it.getProduct().getProductName(), it.getQuantity(), it.getCostPerUnit()))
                .collect(Collectors.toList());
        LOG.info("Получена информация по позициям тендера '{}'", tender);
        return new OperationResultRsDto(OperationResult.SUCCESS, getTenderItemRsDtoList);
    }

    @PostMapping("addTenderItem")
    public OperationResultRsDto addTenderItem(@RequestBody AddTenderItemRqDto rqDto) {
        String tenderNumber = rqDto.getTenderNumber();
        String productName = rqDto.getProductName();
        Tender tender = tenderService.getTenderByTenderNumber(tenderNumber);
        Product product = productService.getProductByProductName(productName);
        TenderItem item = new TenderItem(product, rqDto.getQuantity(), rqDto.getCostPerUnit(), tender);
        tender.addItem(item);
        tenderService.saveTender(tender);
        LOG.info("Позиция тендера '{}' добавлена тендеру '{}'", item, tender);
        String msg = String.format("Позиция добавлена тендеру '%s'", tenderNumber);
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }

    @PutMapping("updateTenderItem")
    public OperationResultRsDto updateTenderItem(@RequestBody UpdateTenderItemRqDto rqDto) {
        String tenderNumber = rqDto.getTenderNumber();
        String productName = rqDto.getProductName();
        Tender tender = tenderService.getTenderByTenderNumber(tenderNumber);
        TenderItem item = tenderService.getTenderItem(tender, productName);
        Integer quantity = item.getQuantity();
        BigDecimal costPerUnit = item.getCostPerUnit();
        String productNameNew = rqDto.getUpdatableData().getProductNameNew();
        Integer quantityNew = rqDto.getUpdatableData().getQuantityNew();
        BigDecimal costPerUnitNew = rqDto.getUpdatableData().getCostPerUnitNew();
        Product productNew = productService.getProductByProductName(productNameNew);
        item.setProduct(productNew);
        item.setQuantity(quantityNew);
        item.setCostPerUnit(costPerUnitNew);
        tenderService.saveTender(tender);
        LOG.info("Позиция тендера '{}' с продуктом '{}' обновлена. Продукт: '{}' -> '{}', Кол-во: '{}' -> '{}', Цена за ед: '{}' -> '{}'",
                tenderNumber, productName, productName, productNameNew, quantity, quantityNew, costPerUnit, costPerUnitNew);
        return new OperationResultRsDto(OperationResult.SUCCESS, "Позиция тендера успешно обновлена");
    }

    @DeleteMapping("removeTenderItem")
    public OperationResultRsDto removeTenderItem(@RequestBody RemoveTenderItemRqDto removeTenderItemRqDto) {
        String tenderNumber = removeTenderItemRqDto.getTenderNumber();
        String productName = removeTenderItemRqDto.getProductName();
        Tender tender = tenderService.getTenderByTenderNumber(tenderNumber);
        tenderService.removeTenderItem(tender, productName);
        tenderService.saveTender(tender);
        LOG.info("Позиция тендера с продуктом '{}' удалена у тендера '{}'", productName, tenderNumber);
        return new OperationResultRsDto(OperationResult.SUCCESS, String.format("Позиция тендера с продуктом '%s' успешно удалена", productName));
    }

}

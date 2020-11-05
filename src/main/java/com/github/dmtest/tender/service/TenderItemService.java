package com.github.dmtest.tender.service;

import com.github.dmtest.tender.domain.Product;
import com.github.dmtest.tender.domain.Tender;
import com.github.dmtest.tender.domain.TenderItem;
import com.github.dmtest.tender.dto.rq.item.AddTenderItemRqDto;
import com.github.dmtest.tender.dto.rq.item.RemoveTenderItemRqDto;
import com.github.dmtest.tender.dto.rq.item.UpdateTenderItemRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.dto.rs.body.GetTenderItemRsDto;
import com.github.dmtest.tender.enums.OperationResult;
import com.github.dmtest.tender.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TenderItemService {
    private static final Logger LOG = LoggerFactory.getLogger(TenderItemService.class);
    private final TenderService tenderService;
    private final ProductService productService;

    @Autowired
    public TenderItemService(TenderService tenderService, ProductService productService) {
        this.tenderService = tenderService;
        this.productService = productService;
    }

    public OperationResultRsDto getTenderItems(String tenderNumber) {
        Tender tender = tenderService.getTenderByTenderNumber(tenderNumber);
        List<GetTenderItemRsDto> getTenderItemRsDtoList = tender.getItems().stream()
                .map(it -> new GetTenderItemRsDto(it.getProduct().getProductName(), it.getQuantity(), it.getCostPerUnit()))
                .collect(Collectors.toList());
        LOG.info("Получена информация по позициям тендера '{}'", tender);
        return new OperationResultRsDto(OperationResult.SUCCESS, getTenderItemRsDtoList);
    }

    public OperationResultRsDto addTenderItem(AddTenderItemRqDto addTenderItemRqDto) {
        String tenderNumber = addTenderItemRqDto.getTenderNumber();
        String productName = addTenderItemRqDto.getProductName();
        Tender tender = tenderService.getTenderByTenderNumber(tenderNumber);
        Product product = productService.getProductByProductName(productName);
        TenderItem item = new TenderItem(product, addTenderItemRqDto.getQuantity(), addTenderItemRqDto.getCostPerUnit(), tender);
        tender.addItem(item);
        tenderService.saveTender(tender);
        LOG.info("Позиция тендера '{}' добавлена тендеру '{}'", item, tender);
        String msg = String.format("Позиция добавлена тендеру '%s'", tenderNumber);
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }

    public OperationResultRsDto updateTenderItem(UpdateTenderItemRqDto updateTenderItemRqDto) {
        String tenderNumber = updateTenderItemRqDto.getTenderNumber();
        String productName = updateTenderItemRqDto.getProductName();
        Tender tender = tenderService.getTenderByTenderNumber(tenderNumber);
        TenderItem item = getTenderItemByProductName(tender, productName);
        Integer quantity = item.getQuantity();
        BigDecimal costPerUnit = item.getCostPerUnit();
        String productNameNew = updateTenderItemRqDto.getUpdatableData().getProductNameNew();
        Integer quantityNew = updateTenderItemRqDto.getUpdatableData().getQuantityNew();
        BigDecimal costPerUnitNew = updateTenderItemRqDto.getUpdatableData().getCostPerUnitNew();
        Product productNew = productService.getProductByProductName(productNameNew);
        item.setProduct(productNew);
        item.setQuantity(quantityNew);
        item.setCostPerUnit(costPerUnitNew);
        tenderService.saveTender(tender);
        LOG.info("Позиция тендера '{}' с продуктом '{}' обновлена. Продукт: '{}' -> '{}', Кол-во: '{}' -> '{}', Цена за ед: '{}' -> '{}'",
                tenderNumber, productName, productName, productNameNew, quantity, quantityNew, costPerUnit, costPerUnitNew);
        return new OperationResultRsDto(OperationResult.SUCCESS, "Позиция тендера успешно обновлена");
    }

    public OperationResultRsDto removeTenderItem(RemoveTenderItemRqDto removeTenderItemRqDto) {
        String tenderNumber = removeTenderItemRqDto.getTenderNumber();
        String productName = removeTenderItemRqDto.getProductName();
        Tender tender = tenderService.getTenderByTenderNumber(tenderNumber);
        TenderItem item = getTenderItemByProductName(tender, productName);
        tender.removeItem(item);
        tenderService.saveTender(tender);
        LOG.info("Позиция тендера с продуктом '{}' удалена у тендера '{}'", productName, tenderNumber);
        return new OperationResultRsDto(OperationResult.SUCCESS, String.format("Позиция тендера с продуктом '%s' успешно удалена", productName));
    }

    private TenderItem getTenderItemByProductName(Tender tender, String productName) {
        return tender.getItem(productName).orElseThrow(() -> new BusinessException(
                OperationResult.TENDER_ITEM_NOT_FOUND, String.format(
                        "Позиция тендера с продуктом '%s' у тендера '%s' не найдена", productName, tender.getTenderNumber())));
    }

}

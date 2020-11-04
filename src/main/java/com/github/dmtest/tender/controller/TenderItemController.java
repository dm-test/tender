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
import com.github.dmtest.tender.exception.BusinessException;
import com.github.dmtest.tender.repo.ProductsRepo;
import com.github.dmtest.tender.repo.TendersRepo;
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
    private final TendersRepo tendersRepo;
    private final ProductsRepo productsRepo;

    @Autowired
    public TenderItemController(TendersRepo tendersRepo, ProductsRepo productsRepo) {
        this.tendersRepo = tendersRepo;
        this.productsRepo = productsRepo;
    }

    @GetMapping("getTenderItems")
    public OperationResultRsDto getTenderItems(@RequestParam("tenderNumber") String tenderNumber) {
        Tender tender = tendersRepo.findByTenderNumber(tenderNumber)
                .orElseThrow(() -> new BusinessException(OperationResult.TENDER_NOT_FOUND, String.format("Тендер с номером '%s' не найден", tenderNumber)));
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
        Tender tender = tendersRepo.findByTenderNumber(tenderNumber)
                .orElseThrow(() -> new BusinessException(OperationResult.TENDER_NOT_FOUND, String.format("Тендер с номером '%s' не найден", tenderNumber)));
        Product product = productsRepo.findByProductName(productName)
                .orElseThrow(() -> new BusinessException(OperationResult.PRODUCT_NOT_FOUND, String.format("Продукт с именем '%s' не найден", productName)));
        TenderItem item = new TenderItem(product, rqDto.getQuantity(), rqDto.getCostPerUnit(), tender);
        tender.addItem(item);
        tendersRepo.save(tender);
        LOG.info("Позиция тендера '{}' добавлена тендеру '{}'", item, tender);
        String msg = String.format("Позиция добавлена тендеру '%s'", tenderNumber);
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }

    @PutMapping("updateTenderItem")
    public OperationResultRsDto updateTenderItem(@RequestBody UpdateTenderItemRqDto rqDto) {
        String tenderNumber = rqDto.getTenderNumber();
        String productName = rqDto.getProductName();
        Tender tender = tendersRepo.findByTenderNumber(tenderNumber)
                .orElseThrow(() -> new BusinessException(OperationResult.TENDER_NOT_FOUND, String.format("Тендер с номером '%s' не найден", tenderNumber)));
        TenderItem item = tender.getItem(productName)
                .orElseThrow(() -> new BusinessException(OperationResult.TENDER_ITEM_NOT_FOUND, String.format("Позиция тендера с продуктом '%s' у тендера '%s' не найдена", productName, tenderNumber)));
        Integer quantity = item.getQuantity();
        BigDecimal costPerUnit = item.getCostPerUnit();
        String productNameNew = rqDto.getUpdatableData().getProductNameNew();
        Integer quantityNew = rqDto.getUpdatableData().getQuantityNew();
        BigDecimal costPerUnitNew = rqDto.getUpdatableData().getCostPerUnitNew();
        Product productNew = productsRepo.findByProductName(productNameNew)
                .orElseThrow(() -> new BusinessException(OperationResult.PRODUCT_NOT_FOUND, String.format("Продукт с именем '%s' не найден", productName)));
        item.setProduct(productNew);
        item.setQuantity(quantityNew);
        item.setCostPerUnit(costPerUnitNew);
        tendersRepo.save(tender);
        LOG.info("Позиция тендера '{}' с продуктом '{}' обновлена. Продукт: '{}' -> '{}', Кол-во: '{}' -> '{}', Цена за ед: '{}' -> '{}'",
                tenderNumber, productName, productName, productNameNew, quantity, quantityNew, costPerUnit, costPerUnitNew);
        return new OperationResultRsDto(OperationResult.SUCCESS, "Позиция тендера успешно обновлена");
    }

    @DeleteMapping("removeTenderItem")
    public OperationResultRsDto removeTenderItem(@RequestBody RemoveTenderItemRqDto removeTenderItemRqDto) {
        String tenderNumber = removeTenderItemRqDto.getTenderNumber();
        String productName = removeTenderItemRqDto.getProductName();
        Tender tender = tendersRepo.findByTenderNumber(tenderNumber)
                .orElseThrow(() -> new BusinessException(OperationResult.TENDER_NOT_FOUND, String.format("Тендер с номером '%s' не найден", tenderNumber)));
        boolean result = tender.removeItem(productName);
        if (!result) {
            throw new BusinessException(OperationResult.TENDER_ITEM_NOT_FOUND, String.format("Позиция тендера с продуктом '%s' у тендера '%s' не найдена", productName, tenderNumber));
        }
        tendersRepo.save(tender);
        LOG.info("Позиция тендера с продуктом '{}' удалена у тендера '{}'", productName, tenderNumber);
        return new OperationResultRsDto(OperationResult.SUCCESS, String.format("Позиция тендера с продуктом '%s' успешно удалена", productName));
    }

}

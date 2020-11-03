package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.domain.Product;
import com.github.dmtest.tender.domain.Tender;
import com.github.dmtest.tender.domain.TenderItem;
import com.github.dmtest.tender.dto.rq.item.AddTenderItemRqDto;
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

import java.util.List;
import java.util.UUID;
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

    @PostMapping("addTenderItem")
    public OperationResultRsDto addTenderItem(@RequestBody AddTenderItemRqDto rqDto) {
        UUID tenderId = rqDto.getTenderId();
        UUID productId = rqDto.getProductId();
        Product product = productsRepo.findById(productId)
                .orElseThrow(() -> new BusinessException(OperationResult.PRODUCT_NOT_FOUND, String.format("Продукт с id '%s' не найден", productId)));
        TenderItem item = new TenderItem(product, rqDto.getQuantity(), rqDto.getCostPerUnit());
        Tender tender = tendersRepo.findById(tenderId)
                .orElseThrow(() -> new BusinessException(OperationResult.TENDER_NOT_FOUND, String.format("Тендер с id '%s' не найден", tenderId)));
        tender.addItem(item);
        tendersRepo.save(tender);
        LOG.info("Позиция тендера добавлена: {}", item);
        String msg = String.format("Позиция добавлена тендеру '%s'", tender.getTenderNumber());
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }

    @GetMapping("getTenderItems")
    public OperationResultRsDto getTenderItems(@RequestParam("tenderId") UUID tenderId) {
        Tender tender = tendersRepo.findById(tenderId)
                .orElseThrow(() -> new BusinessException(OperationResult.TENDER_NOT_FOUND, String.format("Тендер с id '%s' не найден", tenderId)));
        List<TenderItem> items = tender.getItems();
        List<GetTenderItemRsDto> getTenderItemRsDtoList = items.stream()
                .map(it -> new GetTenderItemRsDto(it.getItemId(), it.getProduct().getProductName(), it.getQuantity(), it.getCostPerUnit()))
                .collect(Collectors.toList());
        LOG.info("Получена информация по позициям тендера '{}'", tender);
        return new OperationResultRsDto(OperationResult.SUCCESS, getTenderItemRsDtoList);
    }

    @PutMapping("updateTenderItem")
    public OperationResultRsDto updateTenderItem(@RequestBody UpdateTenderItemRqDto rqDto) {
        UUID tenderId = rqDto.getTenderId();
        UUID itemId = rqDto.getItemId();
        UUID productId = rqDto.getProductId();
        Tender tender = tendersRepo.findById(tenderId)
                .orElseThrow(() -> new BusinessException(OperationResult.TENDER_NOT_FOUND, String.format("Тендер с id '%s' не найден", tenderId)));
        Product product = productsRepo.findById(productId)
                .orElseThrow(() -> new BusinessException(OperationResult.PRODUCT_NOT_FOUND, String.format("Продукт с id '%s' не найден", productId)));
        TenderItem item = tender.getItems().stream()
                .filter(it -> it.getItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new BusinessException(OperationResult.TENDER_ITEM_NOT_FOUND, String.format("Позиция тендера с id '%s' не найдена", itemId)));
        item.setProduct(product);
        item.setQuantity(rqDto.getQuantity());
        item.setCostPerUnit(rqDto.getCostPerUnit());
        tendersRepo.save(tender);
        LOG.info("Позиция тендера обновлена: {}", item);
        return new OperationResultRsDto(OperationResult.SUCCESS, "Позиция тендера успешно обновлена");
    }

}

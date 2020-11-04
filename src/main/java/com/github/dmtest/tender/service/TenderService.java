package com.github.dmtest.tender.service;

import com.github.dmtest.tender.domain.Tender;
import com.github.dmtest.tender.domain.TenderItem;
import com.github.dmtest.tender.enums.OperationResult;
import com.github.dmtest.tender.exception.BusinessException;
import com.github.dmtest.tender.repo.TendersRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenderService {
    private static final Logger LOG = LoggerFactory.getLogger(TenderService.class);
    private final TendersRepo tendersRepo;

    @Autowired
    public TenderService(TendersRepo tendersRepo) {
        this.tendersRepo = tendersRepo;
    }

    public Tender getTenderByTenderNumber(String tenderNumber) {
        return tendersRepo.findByTenderNumber(tenderNumber)
                .orElseThrow(() -> new BusinessException(
                        OperationResult.TENDER_NOT_FOUND, String.format("Тендер с номером '%s' не найден", tenderNumber)));
    }

    public List<Tender> getAllTenders() {
        return tendersRepo.findAll();
    }

    public void saveTender(Tender tender) {
        tendersRepo.save(tender);
    }

    public TenderItem getTenderItem(Tender tender, String productName) {
        return tender.getItem(productName).orElseThrow(() -> new BusinessException(
                OperationResult.TENDER_ITEM_NOT_FOUND, String.format(
                        "Позиция тендера с продуктом '%s' у тендера '%s' не найдена", productName, tender.getTenderNumber())));
    }

    public void removeTenderItem(Tender tender, String productName) {
        boolean result = tender.removeItem(productName);
        if (!result) {
            throw new BusinessException(OperationResult.TENDER_ITEM_NOT_FOUND, String.format(
                    "Позиция тендера с продуктом '%s' у тендера '%s' не найдена", productName, tender.getTenderNumber()));
        }
    }

}

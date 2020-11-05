package com.github.dmtest.tender.service;

import com.github.dmtest.tender.domain.Tender;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.dto.rs.body.GetTenderRsDto;
import com.github.dmtest.tender.enums.OperationResult;
import com.github.dmtest.tender.exception.BusinessException;
import com.github.dmtest.tender.repo.TendersRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TenderService {
    private static final Logger LOG = LoggerFactory.getLogger(TenderService.class);
    private final TendersRepo tendersRepo;

    @Autowired
    public TenderService(TendersRepo tendersRepo) {
        this.tendersRepo = tendersRepo;
    }

    public OperationResultRsDto getAllTenders () {
        List<Tender> tenders = tendersRepo.findAll();
        List<GetTenderRsDto> getTenderRsDtoList = tenders.stream()
                .map(tn -> new GetTenderRsDto(tn.getTenderNumber(), tn.getTenderDate(), tn.getClient().getClientName()))
                .collect(Collectors.toList());
        LOG.info("Получен полный список тендеров");
        return new OperationResultRsDto(OperationResult.SUCCESS, getTenderRsDtoList);
    }

    public Tender getTenderByTenderNumber(String tenderNumber) {
        return tendersRepo.findByTenderNumber(tenderNumber)
                .orElseThrow(() -> new BusinessException(
                        OperationResult.TENDER_NOT_FOUND, String.format("Тендер с номером '%s' не найден", tenderNumber)));
    }

    public void saveTender(Tender tender) {
        tendersRepo.save(tender);
    }

}

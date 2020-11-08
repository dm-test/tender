package com.github.dmtest.tender.service;

import com.github.dmtest.tender.domain.Contract;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.dto.rs.body.GetClientContractRsDto;
import com.github.dmtest.tender.enums.OperationResult;
import com.github.dmtest.tender.exception.BusinessException;
import com.github.dmtest.tender.repo.ContractsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractService {
    private static final Logger LOG = LoggerFactory.getLogger(ContractService.class);
    private final ContractsRepo contractsRepo;

    @Autowired
    public ContractService(ContractsRepo contractsRepo) {
        this.contractsRepo = contractsRepo;
    }

    public OperationResultRsDto getAllContracts() {
        List<Contract> contracts = contractsRepo.findAll();
        List<GetClientContractRsDto> getClientContractRsDtoList = contracts.stream()
                .map(c -> new GetClientContractRsDto(c.getContractNumber(), c.getContractDate(), c.getClient().getClientName()))
                .collect(Collectors.toList());
        LOG.info("Получен полный список контрактов");
        return new OperationResultRsDto(OperationResult.SUCCESS, getClientContractRsDtoList);
    }

    public Contract getContractByContractNumber(String contractNumber) {
        return contractsRepo.findByContractNumber(contractNumber)
                .orElseThrow(() -> new BusinessException(
                        OperationResult.CONTRACT_NOT_FOUND, String.format("Контракт с номером '%s' не найден", contractNumber)));
    }

    public void saveContract(Contract contract) {
        contractsRepo.save(contract);
    }

}

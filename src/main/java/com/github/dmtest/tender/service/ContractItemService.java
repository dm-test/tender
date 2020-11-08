package com.github.dmtest.tender.service;

import com.github.dmtest.tender.domain.Product;
import com.github.dmtest.tender.domain.Contract;
import com.github.dmtest.tender.domain.ContractItem;
import com.github.dmtest.tender.dto.rq.item.AddContractItemRqDto;
import com.github.dmtest.tender.dto.rq.item.RemoveContractItemRqDto;
import com.github.dmtest.tender.dto.rq.item.UpdateContractItemRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.dto.rs.body.GetContractItemRsDto;
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
public class ContractItemService {
    private static final Logger LOG = LoggerFactory.getLogger(ContractItemService.class);
    private final ContractService contractService;
    private final ProductService productService;

    @Autowired
    public ContractItemService(ContractService contractService, ProductService productService) {
        this.contractService = contractService;
        this.productService = productService;
    }

    public OperationResultRsDto getContractItems(String contractNumber) {
        Contract contract = contractService.getContractByContractNumber(contractNumber);
        List<GetContractItemRsDto> getContractItemRsDtoList = contract.getItems().stream()
                .map(it -> new GetContractItemRsDto(it.getProduct().getProductName(), it.getQuantity(), it.getCostPerUnit()))
                .collect(Collectors.toList());
        LOG.info("Получена информация по позициям контракта '{}'", contract);
        return new OperationResultRsDto(OperationResult.SUCCESS, getContractItemRsDtoList);
    }

    public OperationResultRsDto addContractItem(AddContractItemRqDto addContractItemRqDto) {
        String contractNumber = addContractItemRqDto.getContractNumber();
        String productName = addContractItemRqDto.getProductName();
        Contract contract = contractService.getContractByContractNumber(contractNumber);
        Product product = productService.getProductByProductName(productName);
        ContractItem item = new ContractItem(product, addContractItemRqDto.getQuantity(), addContractItemRqDto.getCostPerUnit(), contract);
        contract.addItem(item);
        contractService.saveContract(contract);
        LOG.info("Позиция '{}' добавлена контракту '{}'", item, contract);
        String msg = String.format("Позиция добавлена контракту '%s'", contractNumber);
        return new OperationResultRsDto(OperationResult.SUCCESS, msg);
    }

    public OperationResultRsDto updateContractItem(UpdateContractItemRqDto updateContractItemRqDto) {
        String contractNumber = updateContractItemRqDto.getContractNumber();
        String productName = updateContractItemRqDto.getProductName();
        Contract contract = contractService.getContractByContractNumber(contractNumber);
        ContractItem item = getContractItemByProductName(contract, productName);
        Integer quantity = item.getQuantity();
        BigDecimal costPerUnit = item.getCostPerUnit();
        String productNameNew = updateContractItemRqDto.getUpdatableData().getProductNameNew();
        Integer quantityNew = updateContractItemRqDto.getUpdatableData().getQuantityNew();
        BigDecimal costPerUnitNew = updateContractItemRqDto.getUpdatableData().getCostPerUnitNew();
        Product productNew = productService.getProductByProductName(productNameNew);
        item.setProduct(productNew);
        item.setQuantity(quantityNew);
        item.setCostPerUnit(costPerUnitNew);
        contractService.saveContract(contract);
        LOG.info("Позиция контракта '{}' с продуктом '{}' обновлена. Продукт: '{}' -> '{}', Кол-во: '{}' -> '{}', Цена за ед: '{}' -> '{}'",
                contractNumber, productName, productName, productNameNew, quantity, quantityNew, costPerUnit, costPerUnitNew);
        return new OperationResultRsDto(OperationResult.SUCCESS, "Позиция контракта успешно обновлена");
    }

    public OperationResultRsDto removeContractItem(RemoveContractItemRqDto removeContractItemRqDto) {
        String contractNumber = removeContractItemRqDto.getContractNumber();
        String productName = removeContractItemRqDto.getProductName();
        Contract contract = contractService.getContractByContractNumber(contractNumber);
        ContractItem item = getContractItemByProductName(contract, productName);
        contract.removeItem(item);
        contractService.saveContract(contract);
        LOG.info("Позиция контракта с продуктом '{}' удалена у контракта '{}'", productName, contractNumber);
        return new OperationResultRsDto(OperationResult.SUCCESS, String.format("Позиция контракта с продуктом '%s' успешно удалена", productName));
    }

    private ContractItem getContractItemByProductName(Contract contract, String productName) {
        return contract.getItem(productName).orElseThrow(() -> new BusinessException(
                OperationResult.CONTRACT_ITEM_NOT_FOUND, String.format(
                        "Позиция контракта с продуктом '%s' у контракта '%s' не найдена", productName, contract.getContractNumber())));
    }

}

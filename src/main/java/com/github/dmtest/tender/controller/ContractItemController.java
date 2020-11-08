package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.dto.rq.item.AddContractItemRqDto;
import com.github.dmtest.tender.dto.rq.item.RemoveContractItemRqDto;
import com.github.dmtest.tender.dto.rq.item.UpdateContractItemRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.service.ContractItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("contractItems")
public class ContractItemController {
    private final ContractItemService contractItemService;

    @Autowired
    public ContractItemController(ContractItemService contractItemService) {
        this.contractItemService = contractItemService;
    }

    @GetMapping("getContractItems")
    public OperationResultRsDto getContractItems(@RequestParam("contractNumber") String contractNumber) {
        return contractItemService.getContractItems(contractNumber);
    }

    @PostMapping("addContractItem")
    public OperationResultRsDto addContractItem(@RequestBody AddContractItemRqDto addContractItemRqDto) {
        return contractItemService.addContractItem(addContractItemRqDto);
    }

    @PutMapping("updateContractItem")
    public OperationResultRsDto updateContractItem(@RequestBody UpdateContractItemRqDto updateContractItemRqDto) {
        return contractItemService.updateContractItem(updateContractItemRqDto);
    }

    @DeleteMapping("removeContractItem")
    public OperationResultRsDto removeContractItem(@RequestBody RemoveContractItemRqDto removeContractItemRqDto) {
        return contractItemService.removeContractItem(removeContractItemRqDto);
    }

}

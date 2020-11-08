package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.dto.rq.contract.AddClientContractRqDto;
import com.github.dmtest.tender.dto.rq.contract.RemoveClientContractRqDto;
import com.github.dmtest.tender.dto.rq.contract.UpdateClientContractRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.service.ClientService;
import com.github.dmtest.tender.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("contracts")
public class ContractController {
    private final ClientService clientService;
    private final ContractService contractService;

    @Autowired
    public ContractController(ClientService clientService, ContractService contractService) {
        this.clientService = clientService;
        this.contractService = contractService;
    }

    @GetMapping("getAllContracts")
    public OperationResultRsDto getAllContracts() {
        return contractService.getAllContracts();
    }

    @GetMapping("getClientContracts")
    public OperationResultRsDto getClientContracts(@RequestParam("clientName") String clientName) {
        return clientService.getClientContracts(clientName);
    }

    @GetMapping("getClientContractDetails")
    public OperationResultRsDto getClientContractDetails(@RequestParam("clientName") String clientName,
                                                @RequestParam("contractNumber") String contractNumber) {
        return clientService.getClientContractDetails(clientName, contractNumber);
    }

    @PostMapping("addClientContract")
    public OperationResultRsDto addClientContract(@RequestBody AddClientContractRqDto addClientContractRqDto) {
        return clientService.addClientContract(addClientContractRqDto);
    }

    @PutMapping("updateClientContract")
    public OperationResultRsDto updateClientContract(@RequestBody UpdateClientContractRqDto updateClientContractRqDto) {
        return clientService.updateClientContract(updateClientContractRqDto);
    }

    @DeleteMapping("removeClientContract")
    public OperationResultRsDto removeClientContract(@RequestBody RemoveClientContractRqDto removeClientContractRqDto) {
        return clientService.removeClientContract(removeClientContractRqDto);
    }

}

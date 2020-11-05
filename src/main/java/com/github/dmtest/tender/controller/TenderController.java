package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.dto.rq.tender.AddTenderRqDto;
import com.github.dmtest.tender.dto.rq.tender.RemoveTenderRqDto;
import com.github.dmtest.tender.dto.rq.tender.UpdateTenderRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.service.ClientService;
import com.github.dmtest.tender.service.TenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tenders")
public class TenderController {
    private final ClientService clientService;
    private final TenderService tenderService;

    @Autowired
    public TenderController(ClientService clientService, TenderService tenderService) {
        this.clientService = clientService;
        this.tenderService = tenderService;
    }

    @GetMapping("getAllTenders")
    public OperationResultRsDto getAllTenders() {
        return tenderService.getAllTenders();
    }

    @GetMapping("getClientTenders")
    public OperationResultRsDto getClientTenders(@RequestParam("clientName") String clientName) {
        return clientService.getClientTenders(clientName);
    }

    @GetMapping("getClientTender")
    public OperationResultRsDto getClientTender(@RequestParam("clientName") String clientName,
                                                @RequestParam("tenderNumber") String tenderNumber) {
        return clientService.getClientTender(clientName, tenderNumber);
    }

    @PostMapping("addClientTender")
    public OperationResultRsDto addClientTender(@RequestBody AddTenderRqDto addTenderRqDto) {
        return clientService.addClientTender(addTenderRqDto);
    }

    @PutMapping("updateClientTender")
    public OperationResultRsDto updateClientTender(@RequestBody UpdateTenderRqDto updateTenderRqDto) {
        return clientService.updateClientTender(updateTenderRqDto);
    }

    @DeleteMapping("removeClientTender")
    public OperationResultRsDto removeClientTender(@RequestBody RemoveTenderRqDto removeTenderRqDto) {
        return clientService.removeClientTender(removeTenderRqDto);
    }

}

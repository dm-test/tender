package com.github.dmtest.tender.controller;

import com.github.dmtest.tender.dto.rq.item.AddTenderItemRqDto;
import com.github.dmtest.tender.dto.rq.item.RemoveTenderItemRqDto;
import com.github.dmtest.tender.dto.rq.item.UpdateTenderItemRqDto;
import com.github.dmtest.tender.dto.rs.OperationResultRsDto;
import com.github.dmtest.tender.service.TenderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tenderItems")
public class TenderItemController {
    private final TenderItemService tenderItemService;

    @Autowired
    public TenderItemController(TenderItemService tenderItemService) {
        this.tenderItemService = tenderItemService;
    }

    @GetMapping("getTenderItems")
    public OperationResultRsDto getTenderItems(@RequestParam("tenderNumber") String tenderNumber) {
        return tenderItemService.getTenderItems(tenderNumber);
    }

    @PostMapping("addTenderItem")
    public OperationResultRsDto addTenderItem(@RequestBody AddTenderItemRqDto addTenderItemRqDto) {
        return tenderItemService.addTenderItem(addTenderItemRqDto);
    }

    @PutMapping("updateTenderItem")
    public OperationResultRsDto updateTenderItem(@RequestBody UpdateTenderItemRqDto updateTenderItemRqDto) {
        return tenderItemService.updateTenderItem(updateTenderItemRqDto);
    }

    @DeleteMapping("removeTenderItem")
    public OperationResultRsDto removeTenderItem(@RequestBody RemoveTenderItemRqDto removeTenderItemRqDto) {
        return tenderItemService.removeTenderItem(removeTenderItemRqDto);
    }

}

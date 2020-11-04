package com.github.dmtest.tender.dto.rq.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AddTenderItemRqDto {
    private String tenderNumber;
    private String productName;
    private Integer quantity;
    private BigDecimal costPerUnit;
}

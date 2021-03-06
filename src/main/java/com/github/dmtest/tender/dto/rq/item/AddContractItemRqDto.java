package com.github.dmtest.tender.dto.rq.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AddContractItemRqDto {
    private String contractNumber;
    private String productName;
    private Integer quantity;
    private BigDecimal costPerUnit;
}

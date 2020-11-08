package com.github.dmtest.tender.dto.rs.body;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetContractItemRsDto {
    private final String productName;
    private final Integer quantity;
    private final BigDecimal costPerUnit;
}

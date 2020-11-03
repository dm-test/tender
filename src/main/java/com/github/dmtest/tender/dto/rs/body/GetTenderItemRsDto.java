package com.github.dmtest.tender.dto.rs.body;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class GetTenderItemRsDto {
    private final UUID itemId;
    private final String productName;
    private final Integer quantity;
    private final BigDecimal costPerUnit;
}

package com.github.dmtest.tender.dto.rq.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AddTenderItemRqDto {
    private UUID tenderId;
    private UUID productId;
    private Integer quantity;
    private BigDecimal costPerUnit;
}
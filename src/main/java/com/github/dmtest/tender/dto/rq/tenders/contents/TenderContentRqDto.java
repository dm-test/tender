package com.github.dmtest.tender.dto.rq.tenders.contents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TenderContentRqDto {
    private String productName;
    private Integer quantity;
    private BigDecimal costPerUnit;
}

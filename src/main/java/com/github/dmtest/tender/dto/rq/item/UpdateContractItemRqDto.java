package com.github.dmtest.tender.dto.rq.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class UpdateContractItemRqDto {
    private String contractNumber;
    private String productName;
    private UpdatableData updatableData;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdatableData {
        private String productNameNew;
        private Integer quantityNew;
        private BigDecimal costPerUnitNew;
    }
}

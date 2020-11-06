package com.github.dmtest.tender.dto.rq.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProductRqDto {
    private String productName;
    private UpdatableData updatableData;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdatableData {
        private String productNameNew;
        private String manufacturerNew;
        private String countryNew;
    }

}

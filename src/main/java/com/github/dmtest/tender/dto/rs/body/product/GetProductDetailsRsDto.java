package com.github.dmtest.tender.dto.rs.body.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetProductDetailsRsDto {
    private final String productName;
    private final String manufacturer;
    private final String country;
}

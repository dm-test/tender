package com.github.dmtest.tender.dto.rs.body;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductRsDto {
    private final String productName;
    private final String manufacturer;
    private final String country;
}

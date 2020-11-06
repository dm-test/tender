package com.github.dmtest.tender.dto.rs.body.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetProductRsDto {
    private final List<String> productNames;
}

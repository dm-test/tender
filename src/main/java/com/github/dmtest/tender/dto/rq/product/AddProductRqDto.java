package com.github.dmtest.tender.dto.rq.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddProductRqDto {
    private String productName;
    private String manufacturer;
    private String country;

}

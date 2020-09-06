package com.github.dmtest.tender.dto.rq;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductRqDto {
    private String productName;
    private String manufacturer;
    private String country;

}

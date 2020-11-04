package com.github.dmtest.tender.dto.rq.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RemoveTenderItemRqDto {
    private String tenderNumber;
    private String productName;
}

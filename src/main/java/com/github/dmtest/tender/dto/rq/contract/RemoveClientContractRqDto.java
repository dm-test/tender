package com.github.dmtest.tender.dto.rq.contract;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RemoveClientContractRqDto {
    private String clientName;
    private String contractNumber;
}

package com.github.dmtest.tender.dto.rq.tender;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetTenderRqDto {
    private String clientName;
    private String tenderNumber;
}

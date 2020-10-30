package com.github.dmtest.tender.dto.rq.tender.contents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AddTenderContentsRqDto {
    private String tenderNumber;
    private List<TenderContentRqDto> tenderContents;
}

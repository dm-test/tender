package com.github.dmtest.tender.dto.rq.tenders;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.dmtest.tender.dto.rq.tenders.contents.TenderContentRqDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class AddTenderRqDto {
    private String tenderNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate tenderDate;
    private String clientName;
    private Set<TenderContentRqDto> tenderContents;

}

package com.github.dmtest.tender.dto.rs.body;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class GetTenderDetailsRsDto {
    private final UUID tenderId;
    private final String tenderNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private final LocalDate tenderDate;
    private final List<TenderItemRsDto> tenderItems;
}

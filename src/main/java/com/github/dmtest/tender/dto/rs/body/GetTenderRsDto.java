package com.github.dmtest.tender.dto.rs.body;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class GetTenderRsDto {
    private final String tenderNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private final LocalDate tenderDate;
    private final String clientName;
}

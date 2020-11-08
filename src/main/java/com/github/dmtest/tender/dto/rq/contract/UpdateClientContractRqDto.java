package com.github.dmtest.tender.dto.rq.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UpdateClientContractRqDto {
    private String clientName;
    private String contractNumber;
    private UpdatableData updatableData;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdatableData {
        private String contractNumberNew;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
        private LocalDate contractDateNew;
    }
}

package com.github.dmtest.tender.dto.rq.tender;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UpdateTenderRqDto {
    private String clientName;
    private String tenderNumber;
    private UpdatableData updatableData;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdatableData {
        private String tenderNumberNew;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
        private LocalDate tenderDateNew;
    }
}

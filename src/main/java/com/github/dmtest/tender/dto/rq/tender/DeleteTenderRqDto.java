package com.github.dmtest.tender.dto.rq.tender;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class DeleteTenderRqDto {
    private UUID tenderId;
}

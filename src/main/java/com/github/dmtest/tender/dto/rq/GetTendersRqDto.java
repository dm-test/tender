package com.github.dmtest.tender.dto.rq;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class GetTendersRqDto {
    private UUID clientId;
}

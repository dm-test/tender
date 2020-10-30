package com.github.dmtest.tender.dto.rs.body;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ClientRsDto {
    private final UUID clientId;
    private final String clientName;
}

package com.github.dmtest.tender.dto.rs.body.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetClientDetailsRsDto {
    private final String clientName;
    private final String clientAddress;
}

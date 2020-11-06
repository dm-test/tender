package com.github.dmtest.tender.dto.rs.body.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetClientsRsDto {
    private final List<String> clientNames;
}

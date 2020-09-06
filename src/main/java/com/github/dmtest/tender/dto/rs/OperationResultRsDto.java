package com.github.dmtest.tender.dto.rs;

import com.github.dmtest.tender.enums.OperationResult;
import lombok.Getter;

@Getter
public class OperationResultRsDto {
    private final int resultCode;
    private final String title;
    private final String description;

    public OperationResultRsDto(OperationResult operationResult, String description) {
        resultCode = operationResult.getResultCode();
        title = operationResult.getTitle();
        this.description = description;
    }
}

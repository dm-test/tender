package com.github.dmtest.tender.dto.rs;

import com.github.dmtest.tender.enums.OperationResult;
import lombok.Getter;

@Getter
public class OperationResultRsDto {
    private final int resultCode;
    private final String resultDescription;
    private final Object body;

    public OperationResultRsDto(OperationResult operationResult, Object body) {
        resultCode = operationResult.getResultCode();
        resultDescription = operationResult.getResultDescription();
        this.body = body;
    }
}

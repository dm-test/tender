package com.github.dmtest.tender.exception;

import com.github.dmtest.tender.enums.OperationResult;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final OperationResult operationResult;
    private final String description;

    public BusinessException(OperationResult operationResult, String description) {
        super(String.format("%s >> %s >> %s", operationResult.getResultCode(), operationResult.getTitle(), description));
        this.operationResult = operationResult;
        this.description = description;
    }

}

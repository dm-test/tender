package com.github.dmtest.tender.exception;

import com.github.dmtest.tender.enums.OperationResult;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final OperationResult operationResult;
    private final String detailMessage;

    public BusinessException(OperationResult operationResult, String detailMessage) {
        super(String.format("%s >> %s >> %s", operationResult.getResultCode(), operationResult.getResultDescription(), detailMessage));
        this.operationResult = operationResult;
        this.detailMessage = detailMessage;
    }

}

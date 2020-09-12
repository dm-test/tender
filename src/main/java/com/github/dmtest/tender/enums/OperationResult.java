package com.github.dmtest.tender.enums;

import lombok.Getter;

@Getter
public enum OperationResult {
    SUCCESS(0, "Операция выполнена успешно"),
    CLIENT_NOT_FOUND(1000, "Клиент не найден");

    private final int resultCode;
    private final String resultDescription;

    OperationResult(int resultCode, String resultDescription) {
        this.resultCode = resultCode;
        this.resultDescription = resultDescription;
    }

}

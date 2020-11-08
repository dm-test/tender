package com.github.dmtest.tender.enums;

import lombok.Getter;

@Getter
public enum OperationResult {
    SUCCESS(0, "Операция выполнена успешно"),
    CLIENT_NOT_FOUND(1000, "Клиент не найден"),
    CONTRACT_NOT_FOUND(2000, "Контракт не найден"),
    PRODUCT_NOT_FOUND(3000, "Продукт не найден"),
    CONTRACT_ITEM_NOT_FOUND(4000, "Позиция контракта не найдена");

    private final int resultCode;
    private final String resultDescription;

    OperationResult(int resultCode, String resultDescription) {
        this.resultCode = resultCode;
        this.resultDescription = resultDescription;
    }

}

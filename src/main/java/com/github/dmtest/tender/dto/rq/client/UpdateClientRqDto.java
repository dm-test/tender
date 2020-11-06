package com.github.dmtest.tender.dto.rq.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateClientRqDto {
    private String clientName;
    private UpdatableData updatableData;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdatableData {
        private String clientNameNew;
        private String clientAddressNew;
    }

}

package com.emr.slgi.reception.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum ReceptionStatus {

    WAITING("RE01", "대기"),
    CANCELRECEPTION("RE02", "접수 취소"),
    RECEPTIONCOMPLETE("RE03", "접수 완료");

    private final String code;
    private final String status;

    private static final Map<String, ReceptionStatus> Mapper =
            Arrays.stream(values()).collect(Collectors.toMap(ReceptionStatus::getCode, e -> e));

//    public static ReceptionStatus fromCode(Object o) {
//
//    }

}

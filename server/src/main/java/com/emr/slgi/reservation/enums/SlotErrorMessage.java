package com.emr.slgi.reservation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SlotErrorMessage {

    IS_NOT_EXISTED_TIME("존재하지 않는 시간대입니다.");

    private final String message;

}

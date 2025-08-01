package com.emr.slgi.reservation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationMessage {

    CANCEL_RESERVATION_SUCCESS("예약이 취소됐습니다."),
    SUCCESS_INSERT_RESERVATION("예약이 됐습니다.");

    private final String message;

}

package com.emr.slgi.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public class ReservationErrorMessage {

    public static final String CHOOSE_DOCTOR = "의사를 선택해주세요.";
    public static final String CHOOSE_DATE_TIME = "예약 날짜와 시간을 선택해주세요.";
    public static final String CAN_NOT_FIND_PATIENT = "환자 정보를 찾을 수 없습니다.";
    public static final String WRITE_SYMPTOM = "증상을 입력해주세요.";

    private ReservationErrorMessage() {
        // 생성자 막기
    }
}


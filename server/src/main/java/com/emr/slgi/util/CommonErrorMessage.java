package com.emr.slgi.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommonErrorMessage {

    RETRY("다시 시도해주세요.");

    private final String message;

}

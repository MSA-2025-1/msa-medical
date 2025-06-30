package com.emr.slgi.auth.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Credentials {
    private String userUuid;
    private String id;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private LocalDateTime deleteDate;
}

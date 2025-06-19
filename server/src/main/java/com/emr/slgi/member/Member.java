package com.emr.slgi.member;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Member {
    // TODO: role을 DB에서 가져와서 처리 가능하도록 수정
    private String uuid;
    private String role;
    private String name;
    private String rrn;
    private String phone;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private LocalDateTime deleteDate;
}

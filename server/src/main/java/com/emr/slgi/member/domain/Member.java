package com.emr.slgi.member.domain;

import java.time.LocalDateTime;

import com.emr.slgi.member.enums.MemberRole;

import lombok.Data;

@Data
public class Member {
    private String uuid;
    private MemberRole role;
    private String name;
    private String rrn;
    private String phone;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private LocalDateTime deleteDate;
}

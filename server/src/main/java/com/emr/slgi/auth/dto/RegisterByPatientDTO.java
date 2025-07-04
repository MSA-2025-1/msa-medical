package com.emr.slgi.auth.dto;

import com.emr.slgi.common.constants.RegexPatterns;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterByPatientDTO {
    @NotBlank
    @Size(min = 4, max = 20)
    @Pattern(regexp = RegexPatterns.USERID)
    private String userid;

    @NotBlank
    @Size(min = 4, max = 20)
    @Pattern(regexp = RegexPatterns.PASSWORD)
    private String password;

    @NotBlank
    @Size(max = 20)
    private String name;

    @NotBlank
    @Pattern(regexp = RegexPatterns.RRN)
    private String rrn;

    @NotBlank
    @Pattern(regexp = RegexPatterns.PHONE)
    private String phone;
}

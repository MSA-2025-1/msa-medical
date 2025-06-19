package com.emr.slgi.patient.dto;

import com.emr.slgi.util.ReservationErrorMessage;
import com.emr.slgi.util.Validate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationForm {

    @NotEmpty(message = ReservationErrorMessage.CAN_NOT_FIND_PATIENT)
    @Pattern(regexp = Validate.MEMBER_UUID_REGEX, message = ReservationErrorMessage.CAN_NOT_FIND_PATIENT)
    @Size(min = 36, max=36)
    private String patientUuid; // 테스트용. 추후 JWT 추가되면 삭제.

    @NotEmpty(message = ReservationErrorMessage.CHOOSE_DOCTOR)
    @Pattern(regexp = Validate.MEMBER_UUID_REGEX, message = ReservationErrorMessage.CHOOSE_DOCTOR)
    @Size(min = 36, max = 36)
    private String doctorUuid;

    @NotNull(message = ReservationErrorMessage.CHOOSE_DATE_TIME)
    private LocalDateTime dateTime;

    @NotEmpty(message = ReservationErrorMessage.WRITE_SYMPTOM)
    @Size(min = 1, max = 100)
    private String symptom;

}

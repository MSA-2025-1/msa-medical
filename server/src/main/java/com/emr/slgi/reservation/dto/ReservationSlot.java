package com.emr.slgi.reservation.dto;

import com.emr.slgi.util.ReservationErrorMessage;
import com.emr.slgi.util.Validate;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationSlot {

    @NotEmpty(message = ReservationErrorMessage.CAN_NOT_FIND_PATIENT)
    @Pattern(regexp = Validate.MEMBER_UUID_REGEX, message = ReservationErrorMessage.CAN_NOT_FIND_PATIENT)
    @Size(min = 36, max=36)
    private String patientUuid;

    @NotEmpty(message = ReservationErrorMessage.CHOOSE_DOCTOR)
    @Pattern(regexp = Validate.MEMBER_UUID_REGEX, message = ReservationErrorMessage.CHOOSE_DOCTOR)
    @Size(min = 36, max = 36)
    private String doctorUuid;

    @NotNull(message = ReservationErrorMessage.CHOOSE_DATE_TIME)
    @Min(value = 1, message = ReservationErrorMessage.IS_NOT_VALID_DATE)
    private Long slotId;

    @NotEmpty(message = ReservationErrorMessage.WRITE_SYMPTOM)
    @Size(min = 1, max = 100)
    private String symptom;

}

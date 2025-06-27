package com.emr.slgi.reception.dto;

import com.emr.slgi.util.CommonErrorMessage;
import com.emr.slgi.util.ReceptionErrorMessage;
import com.emr.slgi.util.ReservationErrorMessage;
import com.emr.slgi.util.Validate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static com.emr.slgi.util.ReceptionErrorMessage.*;

@Data
public class ReceptionInfo {

    @NotEmpty
    @Pattern(regexp = Validate.MEMBER_UUID_REGEX, message = ReservationErrorMessage.CHOOSE_DOCTOR)
    private String doctorUuid;

    @NotEmpty
    @Pattern(regexp = Validate.MEMBER_UUID_REGEX, message = ReservationErrorMessage.CAN_NOT_FIND_PATIENT)
    private String patientUuid;

    @NotEmpty
    @Size(min = 2, max = 6)
    private String patientName;

    @NotEmpty
    @Pattern(regexp = Validate.RRN_REGEX, message = ReceptionErrorMessage.RRN_NOT_NULL)
    private String rrn;

    @NotEmpty(message = ReservationErrorMessage.WRITE_SYMPTOM)
    @Size(min = 1, max = 100)
    private String symptom;

    @NotNull(message = ReservationErrorMessage.CHOOSE_DATE_TIME)
    private LocalDateTime dateTime;


}

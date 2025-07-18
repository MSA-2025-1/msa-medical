package com.emr.slgi.reservation.dto;

import com.emr.slgi.reservation.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class ReservationList {

    private String uuid;
    private String patientUuid;
    private String doctorUuid;
    private String name;
    private String slotId;
    private ReservationStatus status;
    private LocalDateTime slot;

}

package com.emr.slgi.reservation.dto;

import com.emr.slgi.reservation.enums.ReservationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationListByStaff {

    private String uuid;
    private String patientUuid;
    private String doctorUuid;
    private String name;
    private ReservationStatus status;
    private LocalDateTime slot;

}

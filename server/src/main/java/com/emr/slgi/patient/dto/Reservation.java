package com.emr.slgi.patient.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Reservation {

    private String id;
    private String patientUuid;
    private String doctorUuid;
    private String status;
    private LocalDateTime reservationDate;
    private String symptom;
    private boolean isToday = false;

}

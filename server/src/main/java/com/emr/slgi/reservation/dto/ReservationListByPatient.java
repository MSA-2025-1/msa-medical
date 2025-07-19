package com.emr.slgi.reservation.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReservationListByPatient {

    private String uuid;
    private String name;
    private LocalDateTime slot;

}

package com.emr.slgi.reservation.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindReservationDate {

    private String patientUuid;
    private String doctorUuid;
    private LocalDateTime dateTime;

}

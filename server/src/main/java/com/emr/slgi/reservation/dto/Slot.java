package com.emr.slgi.reservation.dto;

import com.emr.slgi.reception.enums.ReceptionStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Slot {

    private long id;
    private ReceptionStatus status;
    private LocalDateTime slot;

}

package com.emr.slgi.reservation.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Slot {

    private long id;
    private long slotId;
    private LocalDateTime slot;

}

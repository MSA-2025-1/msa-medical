package com.emr.slgi.reservation.dao;

import com.emr.slgi.reservation.dto.FindReservationDate;
import com.emr.slgi.reservation.dto.Slot;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SlotDAO {

    int checkSlotExistence(Long slotId);

    int selectSlotForUpdate(Long slotId);

    List<Slot> getAllSlots(FindReservationDate reservation);

}

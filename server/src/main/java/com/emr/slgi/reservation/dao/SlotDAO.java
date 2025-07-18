package com.emr.slgi.reservation.dao;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SlotDAO {

    int checkSlotExistence(Long slotId);

    int selectSlotForUpdate(Long slotId);

}

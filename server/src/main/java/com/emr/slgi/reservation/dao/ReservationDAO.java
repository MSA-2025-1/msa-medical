package com.emr.slgi.reservation.dao;

import com.emr.slgi.reservation.dto.*;
import com.emr.slgi.reservation.enums.ReservationStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.emr.slgi.reservation.dto.FindReservationDate;
import com.emr.slgi.reservation.dto.ReservationListByStaff;
import com.emr.slgi.reservation.dto.ReservationListByPatient;

@Mapper
public interface ReservationDAO {

    int makeReservation(ReservationSlot rs);

    List<Slot> getReservationSlots(
            FindReservationDate reservation
    );

    int getAffectedRowsCount(Map<String, Object> reservationData);

    int cancelReservation(@Param("uuidForCancel") Set UuidForCancel);

    List<ReservationListByStaff> getReservationListByStaff(@Param("doctorUuid") String doctorUuid, @Param("date")LocalDateTime date);

    int updateReservationStatus(@Param("uuid") String uuid, @Param("status") ReservationStatus status);

    List<ReservationListByPatient> getReservationListPerPatient(String patientUuid);

    int checkReservation(Long slotId);

}

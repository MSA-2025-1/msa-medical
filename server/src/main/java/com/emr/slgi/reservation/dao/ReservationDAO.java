package com.emr.slgi.reservation.dao;

import com.emr.slgi.reservation.dto.*;
import com.emr.slgi.reservation.enums.ReservationStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.emr.slgi.reservation.dto.FindReservationDate;
import com.emr.slgi.reservation.dto.ReservationForm;
import com.emr.slgi.reservation.dto.ReservationList;
import com.emr.slgi.reservation.dto.ReservationListByPatient;

@Mapper
public interface ReservationDAO {

    int makeReservation(ReservationSlot rs);

    List<ReservationList> getReservationSlots(
            FindReservationDate reservation
    );

    int getAffectedRowsCount(Map<String, Object> reservationData);

    int cancelReservation(@Param("uuidForCancel") Set UuidForCancel);

    List<ReservationList> getFullReservationList(@Param("doctorUuid") String doctorUuid, @Param("date")LocalDateTime date);

    int updateReservationStatus(@Param("uuid") String uuid, @Param("status") ReservationStatus status);

    List<ReservationListByPatient> getReservationListPerPatient(String patientUuid);

    int checkReservation(Long slotId);

}

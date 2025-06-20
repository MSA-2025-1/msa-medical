package com.emr.slgi.patient.dao;

import com.emr.slgi.patient.dto.Reservation;
import com.emr.slgi.patient.dto.ReservationForm;
import com.emr.slgi.patient.dto.ReservationList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface ReservationDAO {

    int makeReservation(ReservationForm rf);

    List<ReservationList> getReservationList(
            Reservation reservation
    );

}

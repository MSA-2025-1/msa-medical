package com.emr.slgi.patient.dao;

import com.emr.slgi.patient.dto.ReservationForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationDAO {

    public int makeReservation(ReservationForm rf);

}

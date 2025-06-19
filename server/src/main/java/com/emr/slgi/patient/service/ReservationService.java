package com.emr.slgi.patient.service;

import com.emr.slgi.patient.dao.ReservationDAO;
import com.emr.slgi.patient.dto.ReservationForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationDAO rDao;

    public int makeReservation(ReservationForm rf) {
        
        return rDao.makeReservation(rf);

    }
}

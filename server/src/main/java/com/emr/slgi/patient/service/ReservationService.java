package com.emr.slgi.patient.service;

import com.emr.slgi.patient.dao.ReservationDAO;
import com.emr.slgi.patient.dto.ReservationForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationDAO rDao;

    public int makeReservation(ReservationForm rf) {
        
        return rDao.makeReservation(rf);

    }
}

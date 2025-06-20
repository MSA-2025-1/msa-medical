package com.emr.slgi.patient.service;

import com.emr.slgi.patient.dao.ReservationDAO;
import com.emr.slgi.patient.dto.Reservation;
import com.emr.slgi.patient.dto.ReservationForm;
import com.emr.slgi.patient.dto.ReservationList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationDAO rDao;

    public int makeReservation(ReservationForm rf) {
        
        return rDao.makeReservation(rf);

    }

    // 환자 직접 등록 시 날짜 선택하면 해당 일자의 예약 데이터 가져오기.
    public Optional<List<ReservationList>> getReservationList(Reservation reservation) {

        reservation.setToday(
            reservation.getReservationDate().toLocalDate().isEqual(LocalDate.now())
        );

        return Optional.ofNullable(rDao.getReservationList(reservation));

    }

}

package com.emr.slgi.reservation.service;

import com.emr.slgi.reception.dao.ReceptionDAO;
import com.emr.slgi.reception.enums.ReceptionStatus;
import com.emr.slgi.reservation.dao.ReservationDAO;
import com.emr.slgi.reservation.dao.SlotDAO;
import com.emr.slgi.reservation.dto.*;
import com.emr.slgi.reservation.enums.ReservationMessage;
import com.emr.slgi.reservation.enums.ReservationStatus;
import com.emr.slgi.reservation.enums.SlotErrorMessage;
import com.emr.slgi.util.CommonErrorMessage;
import com.emr.slgi.util.ReservationErrorMessage;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationDAO rDao;
    private final ReceptionDAO receptionDAO;
    private final SlotDAO slotDAO;
    private final SimpMessageSendingOperations messagingTemplate;

    @Transactional
    public ResponseEntity<Map<String, String>> makeReservation(ReservationSlot rs) {

        if(slotDAO.checkSlotExistence(rs.getSlotId()) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, SlotErrorMessage.IS_NOT_EXISTED_TIME.getMessage());
        }

        slotDAO.selectSlotForUpdate(rs.getSlotId());

        if(rDao.checkReservation(rs.getSlotId()) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ReservationErrorMessage.ALREADY_RESERVED);
        };

        if(rDao.makeReservation(rs) != 1) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ReservationErrorMessage.RESERVATION_RUN_ERROR);
        }

        messagingTemplate.convertAndSend("/sub/status","{}");

        return ResponseEntity.ok(
                Map.of("message", ReservationMessage.SUCCESS_INSERT_RESERVATION.getMessage())
        );

    }

    public int insertReservation(ReservationSlot rs) {

        return rDao.makeReservation(rs);

    }

    public int getAffectedRowsCount(Map reservationData) {

        return rDao.getAffectedRowsCount(reservationData);

    }

    public boolean cancelHoldingReservation(String patientUuid) {

        int affectedRowsCount = getAffectedRowsCount(
                Map.of(
                        "where",
                        "PATIENT_UUID = '" + patientUuid + "' AND STATUS = 'RS03'"
                )
        );

        log.info("취소 개수 : {}", affectedRowsCount);
        log.info("취소할 번호 : {}", patientUuid);

        if(rDao.cancelHoldingReservation(patientUuid) != affectedRowsCount) {
            return false;
        }else{
            return true;
        }


    }

    // 환자 직접 등록 시 날짜 선택하면 해당 일자의 예약 데이터 가져오기.
    public Optional<List<ReservationList>> getReservationList(FindReservationDate reservation) {
        
        // 오늘이면 today 값 true로
        if(reservation.getDateTime().toLocalDate().isEqual(LocalDate.now())) {
            reservation.setToday(true);
        } else {
            reservation.setDateTime(reservation.getDateTime().toLocalDate().atStartOfDay());
        }

        return Optional.ofNullable(rDao.getReservationList(reservation));

    }

    public int holdReservation(FindReservationDate reservationDate) {

        if(!cancelHoldingReservation(reservationDate.getPatientUuid())) {
            return -1;
        }

        return rDao.holdReservation(reservationDate);

    }

    public Optional<List<ReservationList>> getReservationList(LocalDateTime dateTime) {

        return Optional.ofNullable(
            rDao.getReservationListByStaff(
                Map.of(
                    "start", dateTime.toLocalDate().atStartOfDay(),
                    "end", dateTime.toLocalDate().plusDays(1).atStartOfDay()
                )
            )
        );

    }

    public boolean cancelReservation(Set uuidForCancel) {

        if(rDao.cancelReservation(uuidForCancel) < 1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean changeReservation(String reservationId, LocalDateTime dateTime) {

        if(rDao.changeReservation(reservationId, dateTime) != 1) {
            return false;
        }else {
            return true;
        }

    }

    public Optional<List<ReservationList>> getFullReservationList(String doctorUuid, LocalDateTime date) {
        log.info("날짜 : {}", String.valueOf(date));
        return Optional.ofNullable(rDao.getFullReservationList(doctorUuid, date));

    }

    public int updateReservationStatus(String uuid, String updateStatus) {

        ReservationStatus status = ReservationStatus.from(updateStatus);

        return rDao.updateReservationStatus(uuid, status);

    }

    public int updateWaitingStatusOnReception(String uuid, String updateStatus) {

        return receptionDAO.updateWaitingStatusOnReception(uuid, updateStatus);

    }

    public List<ReservationListByPatient> getReservationListPerPatient(String patientUuid) {

        return rDao.getReservationListPerPatient(patientUuid);

    }
}

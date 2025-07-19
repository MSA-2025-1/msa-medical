package com.emr.slgi.reservation.service;

import com.emr.slgi.reception.dao.ReceptionDAO;
import com.emr.slgi.reservation.dao.ReservationDAO;
import com.emr.slgi.reservation.dao.SlotDAO;
import com.emr.slgi.reservation.dto.*;
import com.emr.slgi.reservation.enums.ReservationMessage;
import com.emr.slgi.reservation.enums.ReservationStatus;
import com.emr.slgi.reservation.enums.SlotErrorMessage;
import com.emr.slgi.util.CommonErrorMessage;
import com.emr.slgi.util.ReservationErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationDAO rDao;
    private final ReceptionDAO receptionDAO;
    private final SlotDAO slotDAO;
    private final JdbcTemplate jdbcTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Transactional(timeout = 10)
    public ResponseEntity<Map<String, String>> makeReservation(ReservationSlot rs) {

        jdbcTemplate.execute("SET innodb_lock_wait_timeout = 5");



        if(slotDAO.checkSlotExistence(rs.getSlotId()) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, SlotErrorMessage.IS_NOT_EXISTED_TIME.getMessage());
        }

        if(slotDAO.selectSlotForUpdate(rs.getSlotId()) != 1) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ReservationErrorMessage.RESERVATION_RUN_ERROR + " " + CommonErrorMessage.RETRY);
        }

        if(rDao.checkReservation(rs.getSlotId()) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ReservationErrorMessage.ALREADY_RESERVED);
        };

        if(rDao.makeReservation(rs) != 1) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ReservationErrorMessage.RESERVATION_RUN_ERROR + " " + CommonErrorMessage.RETRY);
        }

        messagingTemplate.convertAndSend("/sub/status","{}");

        return ResponseEntity.ok(
                Map.of("message", ReservationMessage.SUCCESS_INSERT_RESERVATION.getMessage())
        );

    }

    @Transactional
    public List<Slot> getReservationSlots(FindReservationDate reservation) {

        if(reservation.getDateTime().toLocalDate().isAfter(LocalDate.now())) {
            reservation.setDateTime(reservation.getDateTime().toLocalDate().atStartOfDay());
        }

        return rDao.getReservationSlots(reservation);

    }

    @Transactional
    public boolean cancelReservation(Set uuidForCancel) {

        if(rDao.cancelReservation(uuidForCancel) < 1) {
            return false;
        } else {
            return true;
        }

    }

    @Transactional
    public List<ReservationListByStaff> getReservationListByStaff(String doctorUuid, LocalDateTime date) {

        return rDao.getReservationListByStaff(doctorUuid, date);

    }

    @Transactional
    public int updateReservationStatus(String uuid, String updateStatus) {

        ReservationStatus status = ReservationStatus.from(updateStatus);

        return rDao.updateReservationStatus(uuid, status);

    }

    @Transactional
    public int updateWaitingStatusOnReception(String uuid, String updateStatus) {

        return receptionDAO.updateWaitingStatusOnReception(uuid, updateStatus);

    }

    @Transactional
    public List<ReservationListByPatient> getReservationListPerPatient(String patientUuid) {

        return rDao.getReservationListPerPatient(patientUuid);

    }

    @Transactional
    public List<Slot> getAllSlots(FindReservationDate reservation) {

        if(reservation.getDateTime().toLocalDate().isAfter(LocalDate.now())) {
            reservation.setDateTime(reservation.getDateTime().toLocalDate().atStartOfDay());
        }

        return slotDAO.getAllSlots(reservation);

    }
}

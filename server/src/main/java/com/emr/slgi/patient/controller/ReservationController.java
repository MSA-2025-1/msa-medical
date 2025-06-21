package com.emr.slgi.patient.controller;

import com.emr.slgi.patient.dto.Reservation;
import com.emr.slgi.patient.dto.ReservationForm;
import com.emr.slgi.patient.dto.ReservationList;
import com.emr.slgi.patient.service.ReservationService;
import com.emr.slgi.util.CommonErrorMessage;
import com.emr.slgi.util.ReservationErrorMessage;
import com.emr.slgi.util.Validate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/patient")
public class ReservationController {

    private final ReservationService rService;

    @PostMapping("/reservation")
    public ResponseEntity<Map<String, String>> makeReservation(@Valid @RequestBody ReservationForm rf){

        if(rService.makeReservation(rf) != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ReservationErrorMessage.RESERVATION_RUN_ERROR + " " + CommonErrorMessage.RETRY);
        }

        return ResponseEntity.ok(
                Map.of("message", "예약이 완료됐습니다.")
        );

    }

    @PostMapping("/getReservationList")
    public ResponseEntity<Map<String, List<ReservationList>>> getReservationList(@RequestBody Reservation reservation) {

        log.info(reservation.toString());
        log.info("예약 시각: {}", reservation.getReservationDate());
        log.info("현재 시각: {}", LocalDateTime.now());
        log.info("null 결과 : {}", reservation.getReservationDate() == null);
        log.info("비교 결과: {}", reservation.getReservationDate().toLocalDate().isBefore(LocalDate.now()));


        if(reservation.getDoctorUuid() == null || Validate.regexValidate(Map.of(Validate.MEMBER_UUID_REGEX, reservation.getDoctorUuid())).contains(false)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CHOOSE_DOCTOR);
        }

        if(reservation.getReservationDate() == null || reservation.getReservationDate().toLocalDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CAN_NOT_FIND_RESERVATION_DATE);
        }

        return ResponseEntity.ok(
                Map.of("reservationList", rService.getReservationList(reservation).orElse(List.of()))
        );

    }

    // 오버로딩으로 나중에 환자아이디로 예약 목록 가져오기 구현.



}

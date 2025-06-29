package com.emr.slgi.reservation.controller;

import com.emr.slgi.reservation.dto.FindReservationDate;
import com.emr.slgi.reservation.dto.ReservationForm;
import com.emr.slgi.reservation.dto.ReservationList;
import com.emr.slgi.reservation.service.ReservationService;
import com.emr.slgi.util.CommonErrorMessage;
import com.emr.slgi.util.ReservationErrorMessage;
import com.emr.slgi.util.Validate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService rService;

    @PostMapping
    public ResponseEntity<Map<String, String>> makeReservationByPatient(@Valid @RequestBody ReservationForm rf){

        if(rService.makeReservation(rf) != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ReservationErrorMessage.RESERVATION_RUN_ERROR + " " + CommonErrorMessage.RETRY);
        }

        return ResponseEntity.ok(
                Map.of("message", "예약이 완료됐습니다.")
        );

    }

    @PostMapping("/staff")
    public ResponseEntity<Map<String, String>> makeReservationByStaff(@Valid @RequestBody ReservationForm rf){

        if(rService.makeReservation(rf) != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ReservationErrorMessage.RESERVATION_RUN_ERROR + " " + CommonErrorMessage.RETRY);
        }

        return ResponseEntity.ok(
                Map.of("message", "예약이 완료됐습니다.")
        );

    }

    @PostMapping("/hold")
    public ResponseEntity<?> holdReservation(@RequestBody FindReservationDate reservationDate) {

        System.out.println(reservationDate.getDateTime());
        System.out.println(Validate.regexValidate(Map.of(Validate.MEMBER_UUID_REGEX, reservationDate.getDoctorUuid())).contains(false));


        if(reservationDate.getDoctorUuid() == null || Validate.regexValidate(Map.of(Validate.MEMBER_UUID_REGEX, reservationDate.getDoctorUuid())).contains(false)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CHOOSE_DOCTOR);
        }

        if(reservationDate.getDateTime() == null || reservationDate.getDateTime().toLocalDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CAN_NOT_FIND_RESERVATION_DATE);
        }

        if(rService.holdReservation(reservationDate) != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CommonErrorMessage.RETRY);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/getReservationList/{doctorUuid}/{dateTime}")
    public ResponseEntity<Map<String, List<ReservationList>>> getReservationList(
            @PathVariable("doctorUuid") String doctorUuid,
            @PathVariable("dateTime")
            LocalDateTime dateTime) {

        log.info(String.valueOf(dateTime));
        log.info(doctorUuid);

        if(doctorUuid == null || Validate.regexValidate(Map.of(Validate.MEMBER_UUID_REGEX, doctorUuid)).contains(false)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CHOOSE_DOCTOR);
        }

        if(dateTime == null || dateTime.toLocalDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CAN_NOT_FIND_RESERVATION_DATE);
        }

        return ResponseEntity.ok(
                Map.of("reservationList",
                        rService.getReservationList(
                                FindReservationDate.builder().dateTime(dateTime).doctorUuid(doctorUuid).build()
                        ).orElse(List.of())
                )
        );

    }

    @GetMapping("/getReservationList/{dateTime}")
    public ResponseEntity<Map<String, List<ReservationList>>> getReservationList(@PathVariable("dateTime") LocalDateTime dateTime) {

        if(dateTime == null || dateTime.toLocalDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CAN_NOT_FIND_RESERVATION_DATE);
        }

        return ResponseEntity.ok(
                Map.of("reservationList", rService.getReservationList(dateTime).orElse(List.of()))
        );

    }

    @PutMapping("/cancelHoldingReservation")
    public ResponseEntity<?> cancelHoldingReservation() {
        log.info("a");
        String patientUuid = "550e8400-e29b-41d4-a716-446655440020"; // 테스트용 환자 UUID

        if(!rService.cancelHoldingReservation(patientUuid)) {
            log.info("b");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CommonErrorMessage.RETRY);
        }
        log.info("c");

        return ResponseEntity.ok().build();

    }

    @PutMapping("/cancelReservation/{reservationId}")
    public ResponseEntity<?> cancelReservation(@PathVariable("reservationId") String reservationId) {

        if(reservationId == null || Validate.regexValidate(Map.of(Validate.MEMBER_UUID_REGEX, reservationId)).contains(false)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CHOOSE_DOCTOR);
        }

        if(!rService.cancelReservation(reservationId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CommonErrorMessage.RETRY);
        }

        return ResponseEntity.ok().build();

    }

    @PutMapping("/change/{reservationId}/{dateTime}")
    public ResponseEntity<Map<String, String>> changeReservation(@PathVariable("reservationId") String reservationId, @PathVariable("dateTime") LocalDateTime dateTime) {

        if(reservationId == null || Validate.regexValidate(Map.of(Validate.MEMBER_UUID_REGEX, reservationId)).contains(false)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CHOOSE_DOCTOR);
        }

        if(!rService.changeReservation(reservationId, dateTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ReservationErrorMessage.RESERVATION_CHANGE_ERROR + CommonErrorMessage.RETRY);
        }

        return ResponseEntity.ok(
                Map.of("message", "예약이 변경됐습니다.")
        );

    }


    // 오버로딩으로 나중에 환자아이디로 예약 목록 가져오기 구현.
}

package com.emr.slgi.reservation.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emr.slgi.reservation.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.emr.slgi.reception.enums.ReceptionStatus;
import com.emr.slgi.reservation.enums.ReservationMessage;
import com.emr.slgi.reservation.enums.ReservationStatus;
import com.emr.slgi.reservation.service.ReservationService;
import com.emr.slgi.reservation.vo.ReservationCancelForm;
import com.emr.slgi.util.CommonErrorMessage;
import com.emr.slgi.util.ReservationErrorMessage;
import com.emr.slgi.util.Validate;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
	
	private final SimpMessageSendingOperations messagingTemplate;
    private final ReservationService rService;

    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'NURSE')")
    @PostMapping
    public ResponseEntity<Map<String, String>> makeReservation(@Valid @RequestBody ReservationSlot rs){ // 시간대는 슬롯 id로 넘겨준다.

        return rService.makeReservation(rs);

    }

    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'NURSE')")
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

    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'NURSE')")
    @GetMapping("/getReservationList/{doctorUuid}/{dateTime}")
    public ResponseEntity<Map<String, List<ReservationList>>> getReservationList(
            @PathVariable("doctorUuid") String doctorUuid,
            @PathVariable("dateTime") LocalDateTime dateTime) {

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

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/getReservationList/{dateTime}")
    public ResponseEntity<Map<String, List<ReservationList>>> getReservationList(@PathVariable("dateTime") LocalDateTime dateTime) {

        if(dateTime == null || dateTime.toLocalDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CAN_NOT_FIND_RESERVATION_DATE);
        }

        return ResponseEntity.ok(
                Map.of("reservationList", rService.getReservationList(dateTime).orElse(List.of()))
        );

    }

    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'NURSE')")
    @PutMapping("/cancelHoldingReservation")
    public ResponseEntity<?> cancelHoldingReservation(@RequestBody ReservationList rl) {

        if(rl.getPatientUuid() == null || Validate.regexValidate(Map.of(Validate.MEMBER_UUID_REGEX, rl.getPatientUuid())).contains(false)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CAN_NOT_FIND_PATIENT);
        }

        if(!rService.cancelHoldingReservation(rl.getPatientUuid())) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CommonErrorMessage.RETRY);
        }

        return ResponseEntity.ok().build();

    }

    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping("/cancel")
    public ResponseEntity<?> cancelReservation(@RequestBody @Valid ReservationCancelForm rcf) {

        if(rcf == null || Validate.regexValidation(Map.of(Validate.MEMBER_UUID_REGEX, rcf.getUuidForCancel())).contains(false)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CAN_NOT_FIND_RESERVATION_DATE);
        }

        if(!rService.cancelReservation(rcf.getUuidForCancel())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CommonErrorMessage.RETRY);
        }
        messagingTemplate.convertAndSend("/sub/status","{}");
        return ResponseEntity.ok(
                Map.of(
                        "message", ReservationMessage.CANCEL_RESERVATION_SUCCESS.getMessage()
                )
        );

    }

    @PreAuthorize("hasRole('PATIENT')")
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

    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE')")
    @GetMapping("/{uuid}/{date}/list")
    public ResponseEntity<Map<String, List<ReservationList>>> getFullReservationList(
            @PathVariable("uuid") String doctorUuid,
            @PathVariable("date") LocalDateTime date
    ) {

        log.info(String.valueOf(date));

        if(doctorUuid == null || Validate.regexValidate(Map.of(Validate.MEMBER_UUID_REGEX, doctorUuid)).contains(false)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CAN_NOT_FIND_DOCTOR_INFO + CommonErrorMessage.RETRY);
        }

        List<ReservationList> reservationList = rService.getFullReservationList(doctorUuid, date).get();

        if(reservationList == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ReservationErrorMessage.CAN_NOT_FIND_RESERVATION_DATE + CommonErrorMessage.RETRY);
        }

        return ResponseEntity.ok(
                Map.of(
                        "reservationList", reservationList
                )
        );

    } 
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE')")
    @PutMapping("/{uuid}/{updateStatus}/updateStatus")
    public ResponseEntity<?> updateReservationStatus(
            @PathVariable("uuid") String uuid,
            @PathVariable("updateStatus") String updateStatus
    ) {

        Map<String, String> message = new HashMap<>();
        Map<String, Boolean> statusChk = new HashMap<>();

        if(uuid == null || Validate.regexValidate(Map.of(Validate.MEMBER_UUID_REGEX, uuid)).contains(false)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CAN_NOT_FIND_PATIENT + CommonErrorMessage.RETRY);
        }

        if(ReservationStatus.fromStatusTextExists(updateStatus.trim())) {
            statusChk.put(updateStatus.trim(), true);
        }

        if(ReceptionStatus.fromStatusTextExists(updateStatus.trim())) {
            updateStatus = ReservationStatus.fromCode("RS04").getStatus();
            statusChk.put(updateStatus.trim(), true);
        }

        if(rService.updateReservationStatus(uuid, updateStatus) != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CommonErrorMessage.UPDATE_ERR + CommonErrorMessage.RETRY);
        }


        if (statusChk.containsKey(ReservationStatus.fromCode("RS02").getStatus())) {
            message.put("message", ReservationMessage.CANCEL_RESERVATION_SUCCESS.getMessage());
        }

        if (statusChk.containsKey(ReservationStatus.fromCode("RS04").getStatus())) {

            if (rService.updateWaitingStatusOnReception(uuid, updateStatus) != 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CommonErrorMessage.UPDATE_ERR + CommonErrorMessage.RETRY);
            }

        }
        messagingTemplate.convertAndSend("/sub/status", "{}");
        
        
        return ResponseEntity.ok(
                message
        );  

    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/{uuid}/patientlist")
    public ResponseEntity<Map<String, List<ReservationListByPatient>>> getReservationListPerPatient(
            @PathVariable("uuid") String patientUuid
    ) {

        if(patientUuid == null || Validate.regexValidate(Map.of(Validate.MEMBER_UUID_REGEX, patientUuid)).contains(false)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CAN_NOT_FIND_PATIENT + CommonErrorMessage.RETRY);
        }

        List<ReservationListByPatient> list = rService.getReservationListPerPatient(patientUuid);

        if(list == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CAN_NOT_FIND_RESERVATION_DATE);
        }

        return ResponseEntity.ok(
                Map.of(
                    "patientReservationList", list
                )
        );
    }



}

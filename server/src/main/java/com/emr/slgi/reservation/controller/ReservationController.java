package com.emr.slgi.reservation.controller;

import com.emr.slgi.reception.enums.ReceptionStatus;
import com.emr.slgi.reservation.dto.*;
import com.emr.slgi.reservation.enums.ReservationMessage;
import com.emr.slgi.reservation.enums.ReservationStatus;
import com.emr.slgi.reservation.service.ReservationService;
import com.emr.slgi.reservation.vo.ReservationCancelForm;
import com.emr.slgi.util.CommonErrorMessage;
import com.emr.slgi.util.ReservationErrorMessage;
import com.emr.slgi.util.Validate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
	
	private final SimpMessageSendingOperations messagingTemplate;
    private final ReservationService rService;

    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'NURSE')")
    @PostMapping
    public ResponseEntity<Map<String, String>> makeReservation(@Valid @RequestBody ReservationSlot rs){ 

        return rService.makeReservation(rs);

    }

    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'NURSE')")
    @GetMapping("/getReservationList/{doctorUuid}/{dateTime}")
    public ResponseEntity<Map<String, List<Slot>>> getReservationSlots(
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
                        rService.getReservationSlots(
                                FindReservationDate.builder().dateTime(dateTime).doctorUuid(doctorUuid).build()
                        )
                )
        );

    }

    @PreAuthorize("hasAnyRole('DOCTOR','PATIENT', 'NURSE')")
    @GetMapping("/allSlots/{dateTime}")
    public ResponseEntity<?> getAllSlots(@PathVariable("dateTime") LocalDateTime dateTime) {

        if(dateTime == null || dateTime.toLocalDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CAN_NOT_FIND_RESERVATION_DATE);
        }

        return ResponseEntity.ok(
                Map.of("allSlots", rService.getAllSlots(FindReservationDate.builder().dateTime(dateTime).build()))
        );

    }

    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping("/cancel")
    public ResponseEntity<?> cancelReservation(@RequestBody @Valid ReservationCancelForm rcf) {

        if(Validate.regexValidation(Map.of(Validate.MEMBER_UUID_REGEX, rcf.getUuidForCancel())).contains(false)) {
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

    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE')")
    @GetMapping("/{uuid}/{date}/listByStaff")
    public ResponseEntity<Map<String, List<ReservationListByStaff>>> getReservationListByStaff(
            @PathVariable("uuid") String doctorUuid,
            @PathVariable("date") LocalDateTime date
    ) {

        if(doctorUuid == null || Validate.regexValidate(Map.of(Validate.MEMBER_UUID_REGEX, doctorUuid)).contains(false)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CAN_NOT_FIND_DOCTOR_INFO + CommonErrorMessage.RETRY);
        }

        List<ReservationListByStaff> list = rService.getReservationListByStaff(doctorUuid, date);

        return ResponseEntity.ok(
                Map.of(
                        "reservationList", list
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
    @GetMapping("/{uuid}/listByPatient")
    public ResponseEntity<Map<String, List<ReservationListByPatient>>> getReservationListPerPatient(
            @PathVariable("uuid") String patientUuid
    ) {

        if(patientUuid == null || Validate.regexValidate(Map.of(Validate.MEMBER_UUID_REGEX, patientUuid)).contains(false)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ReservationErrorMessage.CAN_NOT_FIND_PATIENT + " " + CommonErrorMessage.RETRY);
        }

        List<ReservationListByPatient> list = rService.getReservationListPerPatient(patientUuid);

        return ResponseEntity.ok(
                Map.of(
                    "patientReservationList", list
                )
        );
    }

}

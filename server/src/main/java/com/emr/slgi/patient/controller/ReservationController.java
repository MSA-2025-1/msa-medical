package com.emr.slgi.patient.controller;

import com.emr.slgi.patient.dto.ReservationForm;
import com.emr.slgi.patient.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/patient")
public class ReservationController {

    private final ReservationService rService;

    @GetMapping("/reservation")
    public ResponseEntity<Map<String, String>> makeReservation(@Valid ReservationForm rf){

        if(rService.makeReservation(rf) != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "예약 중 문제가 발생했습니다. 다시 시도해주세요.");
        }

        return ResponseEntity.ok().body(Map.of("message", "예약이 완료됐습니다."));

    }



}

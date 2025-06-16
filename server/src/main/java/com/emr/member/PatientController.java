package com.emr.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PatientController {

    private final PatientService ps;


    @GetMapping("/getDoctorList")
    public ResponseEntity<Map<String, Object>> getDoctorList(){

        Optional<List> doctorList = ps.getDoctorList();

        if(doctorList.isEmpty()) throw

        return ResponseEntity.ok().body(Map.of("doctorList", doctorList));

    }

    @GetMapping("/registerReservationByPatient")
    public ResponseEntity<Map<String, Object>> registerReservationByPatient(){




        return null;
    }



}

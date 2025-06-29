package com.emr.slgi.patient;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emr.slgi.patient.dto.PatientRegisterDTO;
import com.emr.slgi.patient.dto.PatientSearchDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<?> registerByStaff(@RequestBody PatientRegisterDTO patientRegisterDTO) {
        patientService.createPatient(patientRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("search")
    public ResponseEntity<?> search(PatientSearchDTO patientSearchDTO) {
        return ResponseEntity.ok(Map.of("list", patientService.search(patientSearchDTO)));
    }
}

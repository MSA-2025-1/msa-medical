package com.emr.slgi.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emr.slgi.member.dto.PatientRegisterDTO;
import com.emr.slgi.member.dto.PatientSearchDTO;
import com.emr.slgi.member.dto.UpdatePatientProfileRequest;
import com.emr.slgi.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    private final MemberService memberService;

    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE')")
    @PostMapping
    public ResponseEntity<?> registerByStaff(@RequestBody @Valid PatientRegisterDTO patientRegisterDTO) {
        memberService.createPatient(patientRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE')")
    @GetMapping("/search")
    public ResponseEntity<?> search(@Valid PatientSearchDTO patientSearchDTO) {
        return ResponseEntity.ok(memberService.search(patientSearchDTO));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal String uuid) {
        return ResponseEntity.ok(memberService.getProfile(uuid));
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PatchMapping("/profile")
    public ResponseEntity<?> updateProfile(
        @AuthenticationPrincipal String uuid,
        @RequestBody @Valid UpdatePatientProfileRequest updatePatientProfileRequest
    ) {
        memberService.updateProfile(uuid, updatePatientProfileRequest);
        return ResponseEntity.noContent().build();
    }
}

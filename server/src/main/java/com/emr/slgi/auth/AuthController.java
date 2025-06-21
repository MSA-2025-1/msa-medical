package com.emr.slgi.auth;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emr.slgi.auth.dto.RegisterByPatientDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register/patient")
    public ResponseEntity<?> registerByPatient(@RequestBody RegisterByPatientDTO registerByPatient) {
        authService.registerByPatient(registerByPatient);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/check-id")
    public ResponseEntity<?> checkIdDuplicate(@RequestParam String userid) {
        boolean exists = authService.checkIdDuplicate(userid);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    // @PostMapping
    // public ResponseEntity<?> createByStaff(CreateByStaffDTO createByStaffDTO) {
    //     return ResponseEntity.status(HttpStatus.CREATED).build();
    // }
}

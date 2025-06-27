package com.emr.slgi.reception.controller;

import com.emr.slgi.reception.dto.ReceptionInfo;
import com.emr.slgi.reception.service.ReceptionService;
import com.emr.slgi.reception.service.WaitingList;
import com.emr.slgi.util.CommonErrorMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/reception")
public class ReceptionController {

    private final ReceptionService receptionService;

    @PreAuthorize("hasRole('DOCTOR', 'NURSE')")
    @PostMapping
    public ResponseEntity<?> acceptPatientByStaff(@RequestBody @Valid ReceptionInfo receptionInfo) {

        if(receptionService.acceptPatientByStaff(receptionInfo) != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CommonErrorMessage.RETRY);
        }

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('DOCTOR', 'NURSE')")
    @GetMapping("/getWaitingList")
    public ResponseEntity<Map<String, List<WaitingList>>> getWaitingList() {

        List<WaitingList> list = receptionService.getWaitingList();

        return ResponseEntity.ok(
                Map.of("waitingList", list)
        );

    }

}

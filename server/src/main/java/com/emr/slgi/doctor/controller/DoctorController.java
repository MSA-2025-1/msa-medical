package com.emr.slgi.doctor.controller;

import java.util.List;
import java.util.Map;

import com.emr.slgi.doctor.dto.DoctorList;
import com.emr.slgi.doctor.service.DoctorService;
import com.emr.slgi.member.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emr.slgi.member.MemberService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService dService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, List<DoctorList>>> getDoctorList() {
        List<DoctorList> list = dService.getDoctorList();
        if(list.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "다시 시도해주세요.");
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("list", list));
    }


}

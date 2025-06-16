package com.emr.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PatientService {

    private final PatientMapper pm;

    public Optional<List> getDoctorList(){
        return pm.getDoctorList();
    }


}

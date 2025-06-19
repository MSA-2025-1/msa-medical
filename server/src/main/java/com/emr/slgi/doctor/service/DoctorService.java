package com.emr.slgi.doctor.service;

import com.emr.slgi.doctor.dao.DoctorDAO;
import com.emr.slgi.doctor.dto.DoctorList;
import com.emr.slgi.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorDAO dDao;

    public List<DoctorList> getDoctorList() {
        return dDao.getDoctorList();
    }

}

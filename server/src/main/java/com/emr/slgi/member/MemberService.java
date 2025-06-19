package com.emr.slgi.member;

import java.util.List;

import com.emr.slgi.patient.dto.ReservationForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDAO memberDAO;

    public Member getById(String id) {
        return memberDAO.getById(id);
    }

}

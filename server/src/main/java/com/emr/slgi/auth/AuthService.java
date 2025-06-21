package com.emr.slgi.auth;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.emr.slgi.auth.dto.RegisterByPatientDTO;
import com.emr.slgi.credentials.CredentialsDAO;
import com.emr.slgi.credentials.dto.CredentialsCreateDTO;
import com.emr.slgi.member.MemberDAO;
import com.emr.slgi.member.dto.MemberCreateDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberDAO memberDAO;
    private final CredentialsDAO credentialsDAO;

    public void registerByPatient(RegisterByPatientDTO registerByPatientDTO) {
        // TODO: transaction 추가
        // TODO: 주민번호, 전화번호 중복 확인 후 처리
        // TODO: 아이디 중복 확인 후 처리
        String uuid = UUID.randomUUID().toString();
        MemberCreateDTO memberCreateDTO = new MemberCreateDTO(
            uuid,
            registerByPatientDTO.getName(),
            registerByPatientDTO.getRrn(),
            registerByPatientDTO.getPhone()
        );
        memberDAO.createPatient(memberCreateDTO);
        CredentialsCreateDTO credentialsCreateDTO = new CredentialsCreateDTO(
            uuid,
            registerByPatientDTO.getUserid(),
            registerByPatientDTO.getPassword()
        );
        credentialsDAO.create(credentialsCreateDTO);
    }

    public boolean checkIdDuplicate(String userid) {
        return credentialsDAO.existsByUserid(userid);
    }

    // public void createByStaff() {
    //     memberDAO.createPatient();
    // }
}

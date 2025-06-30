package com.emr.slgi.member.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.emr.slgi.auth.dto.RegisterByPatientDTO;
import com.emr.slgi.member.dao.MemberDAO;
import com.emr.slgi.member.domain.Member;
import com.emr.slgi.member.dto.MemberCreateDTO;
import com.emr.slgi.member.dto.MemberSearchDTO;
import com.emr.slgi.member.dto.PatientRegisterDTO;
import com.emr.slgi.member.dto.PatientSearchDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberDAO memberDAO;

  public Member getByUuid(String uuid) {
    return memberDAO.getByUuid(uuid);
  }

  public List<Member> getDoctorList() {
    return memberDAO.getDoctorList();
  }

  public String getDoctorName(String uuid) {
    String name = memberDAO.getDoctorName(uuid);
    if (name == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "의사를 찾을 수 없습니다.");
    }
    return name;
  }

  public List<Member> getStaffList(String uuid) {
    List<Member> staffList = memberDAO.getStaffList(uuid);
    if (staffList.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return staffList;
  }

  public String createPatient(RegisterByPatientDTO registerByPatientDTO) {
    String uuid = UUID.randomUUID().toString();
    MemberCreateDTO memberCreateDTO = new MemberCreateDTO(
      uuid,
      registerByPatientDTO.getName(),
      registerByPatientDTO.getRrn(),
      registerByPatientDTO.getPhone()
    );
    memberDAO.createPatient(memberCreateDTO);
    return uuid;
  }

  public void createPatient(PatientRegisterDTO patientRegisterDTO) {
    String uuid = UUID.randomUUID().toString();
    MemberCreateDTO memberCreateDTO = new MemberCreateDTO(
      uuid,
      patientRegisterDTO.getName(),
      patientRegisterDTO.getRrn(),
      patientRegisterDTO.getPhone()
    );
    memberDAO.createPatient(memberCreateDTO);
  }

  public List<Member> search(PatientSearchDTO patientSearchDTO) {
    MemberSearchDTO memberSearchDTO = new MemberSearchDTO(patientSearchDTO.getSearchValue());
    return memberDAO.search(memberSearchDTO);
  }

}

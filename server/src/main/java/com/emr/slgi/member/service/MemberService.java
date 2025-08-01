package com.emr.slgi.member.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.emr.slgi.common.dto.ListResponse;
import com.emr.slgi.member.dao.MemberDAO;
import com.emr.slgi.member.domain.Member;
import com.emr.slgi.member.dto.DoctorUuidName;
import com.emr.slgi.member.dto.MemberCreateDTO;
import com.emr.slgi.member.dto.MemberProfileResponse;
import com.emr.slgi.member.dto.MemberSearchDTO;
import com.emr.slgi.member.dto.PatientRegisterDTO;
import com.emr.slgi.member.dto.PatientSearchDTO;
import com.emr.slgi.member.dto.PatientSummary;
import com.emr.slgi.member.dto.StaffSummary;
import com.emr.slgi.member.dto.UpdatePatientProfile;
import com.emr.slgi.member.dto.UpdatePatientProfileRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberDAO memberDAO;

  public Member getByUuid(String uuid) {
    return memberDAO.getByUuid(uuid);
  }

  public Optional<String> getUuidByRrn(String rrn) {
    return memberDAO.getUuidByRrn(rrn);
  }

  public ListResponse<DoctorUuidName> getDoctorList() {
    List<DoctorUuidName> list = memberDAO.getDoctorList();
    if (list.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "의사가 없습니다.");
    }
    return new ListResponse<>(list);
  }

  public ListResponse<StaffSummary> getOtherStaffList(String uuid) {
    List<StaffSummary> staffList = memberDAO.getOtherStaffList(uuid);
    if (staffList.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return new ListResponse<>(staffList);
  }

  public String createPatient(PatientRegisterDTO patientRegisterDTO) {
    String uuid = UUID.randomUUID().toString();
    MemberCreateDTO memberCreateDTO = new MemberCreateDTO(
      uuid,
      patientRegisterDTO.getName(),
      patientRegisterDTO.getRrn(),
      patientRegisterDTO.getPhone()
    );
    try {
      memberDAO.createPatient(memberCreateDTO);
    } catch (Exception exception) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 주민번호입니다.");
    }
    return uuid;
  }

  public ListResponse<PatientSummary> search(PatientSearchDTO patientSearchDTO) {
    MemberSearchDTO memberSearchDTO = new MemberSearchDTO(patientSearchDTO.getSearchValue());
    return new ListResponse<>(memberDAO.search(memberSearchDTO));
  }

  public MemberProfileResponse getProfile(String uuid) {
    return new MemberProfileResponse(memberDAO.getProfile(uuid));
  }

  public void updateProfile(String uuid, UpdatePatientProfileRequest updatePatientProfileRequest) {
    UpdatePatientProfile updatePatientProfile = new UpdatePatientProfile();
    updatePatientProfile.setUuid(uuid);
    updatePatientProfile.setName(updatePatientProfileRequest.getName());
    updatePatientProfile.setPhone(updatePatientProfileRequest.getPhone());
    memberDAO.updateProfile(updatePatientProfile);
  }

}

package com.emr.slgi.member.dao;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.emr.slgi.member.domain.Member;
import com.emr.slgi.member.dto.DoctorUuidName;
import com.emr.slgi.member.dto.MemberCreateDTO;
import com.emr.slgi.member.dto.MemberProfile;
import com.emr.slgi.member.dto.MemberSearchDTO;
import com.emr.slgi.member.dto.PatientSummary;
import com.emr.slgi.member.dto.StaffSummary;
import com.emr.slgi.member.dto.UpdatePatientProfile;

@Mapper
public interface MemberDAO {
    public Member getByUuid(String uuid);
    public Optional<String> getUuidByRrn(String rrn);
    public List<DoctorUuidName> getDoctorList();
    public List<StaffSummary> getOtherStaffList(String uuid);
    public List<PatientSummary> search(MemberSearchDTO memberSearchDTO);
    public void createPatient(MemberCreateDTO memberCreateDTO);
    public MemberProfile getProfile(String uuid);
    public void updateProfile(UpdatePatientProfile updatePatientProfile);
}

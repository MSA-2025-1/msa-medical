package com.emr.slgi.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.emr.slgi.member.domain.Member;
import com.emr.slgi.member.dto.MemberCreateDTO;
import com.emr.slgi.member.dto.MemberSearchDTO;

@Mapper
public interface MemberDAO {
    public Member getByUuid(String uuid);
    public List<Member> getDoctorList();
    public List<Member> getStaffList(String uuid);
    public List<Member> search(MemberSearchDTO memberSearchDTO);
    public void createPatient(MemberCreateDTO memberCreateDTO);
    public String getDoctorName(String uuid);
}

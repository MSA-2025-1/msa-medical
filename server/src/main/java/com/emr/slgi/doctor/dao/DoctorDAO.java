package com.emr.slgi.doctor.dao;

import com.emr.slgi.doctor.dto.DoctorList;
import com.emr.slgi.member.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DoctorDAO {

    public List<DoctorList> getDoctorList();

}

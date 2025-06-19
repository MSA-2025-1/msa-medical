package com.emr.slgi.member;

import java.util.List;

import com.emr.slgi.patient.dto.ReservationForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDAO {

    public Member getById(String id);

}

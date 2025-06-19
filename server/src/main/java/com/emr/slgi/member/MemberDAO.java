package com.emr.slgi.member;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDAO {

    public Member getById(String id);

}

package com.emr.slgi.auth.dao;

import org.apache.ibatis.annotations.Mapper;

import com.emr.slgi.auth.domain.Credentials;
import com.emr.slgi.auth.dto.CredentialsCreateDTO;

@Mapper
public interface CredentialsDAO {
    public void create(CredentialsCreateDTO credentialsCreateDTO);
    public boolean existsByUserid(String userid);
    public Credentials getMemberCredentials(String userid);
}

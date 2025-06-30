package com.emr.slgi.auth.service;

import org.springframework.stereotype.Service;

import com.emr.slgi.auth.dao.CredentialsDAO;
import com.emr.slgi.auth.domain.Credentials;
import com.emr.slgi.auth.dto.CredentialsCreateDTO;
import com.emr.slgi.auth.dto.LoginDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CredentialsService {

    private final CredentialsDAO credentialsDAO;

    public void create(CredentialsCreateDTO credentialsCreateDTO) {
        credentialsDAO.create(credentialsCreateDTO);
    }

    public boolean existsByUserid(String userid) {
        return credentialsDAO.existsByUserid(userid);
    }

    public Credentials getMemberCredentials(LoginDTO loginDTO) {
        return credentialsDAO.getMemberCredentials(loginDTO.getUserid());
    }
}

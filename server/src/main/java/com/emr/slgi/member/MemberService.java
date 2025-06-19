package com.emr.slgi.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDAO memberDAO;

    public Member getById(String id) {
        return memberDAO.getById(id);
    }

}

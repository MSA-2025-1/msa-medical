package com.emr.slgi.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emr.slgi.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/me")
  public ResponseEntity<?> me(@AuthenticationPrincipal String uuid) {
    // TODO: 추후 컬럼 축소
    return ResponseEntity.ok(memberService.getByUuid(uuid));
  }

}

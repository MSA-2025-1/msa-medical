package com.emr.slgi.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emr.slgi.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {
  private final MemberService memberService;

  @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE')")
  @GetMapping("/list")
  public ResponseEntity<?> getOtherStaffList(@AuthenticationPrincipal String uuid) {
    return ResponseEntity.ok(memberService.getOtherStaffList(uuid));
  }
}

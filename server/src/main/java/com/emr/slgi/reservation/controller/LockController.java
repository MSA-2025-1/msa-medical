package com.emr.slgi.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locks")
@RequiredArgsConstructor
public class LockController {

    private final LockService lockService;




}

package com.emr.slgi.status;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatusService {
	private final StatusDAO statusDAO;

}

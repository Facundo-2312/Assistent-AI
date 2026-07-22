package com.facundo.assistentia.interfaces.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.facundo.assistentia.application.system.dto.PlatformInfoResponse;
import com.facundo.assistentia.application.system.service.PlatformInfoService;

@RestController
@RequestMapping("/api/v1/system")
public class SystemController {

	private final PlatformInfoService platformInfoService;

	public SystemController(PlatformInfoService platformInfoService) {
		this.platformInfoService = platformInfoService;
	}

	@GetMapping("/info")
	public ResponseEntity<PlatformInfoResponse> info() {
		return ResponseEntity.ok(platformInfoService.getPlatformInfo());
	}
}
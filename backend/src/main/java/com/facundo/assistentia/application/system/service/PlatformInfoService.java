package com.facundo.assistentia.application.system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.facundo.assistentia.application.system.dto.PlatformInfoResponse;

@Service
public class PlatformInfoService {

	private final String applicationName;
	private final String environment;
	private final String version;

	public PlatformInfoService(
		@Value("${spring.application.name}") String applicationName,
		@Value("${app.environment}") String environment,
		@Value("${app.version}") String version
	) {
		this.applicationName = applicationName;
		this.environment = environment;
		this.version = version;
	}

	public PlatformInfoResponse getPlatformInfo() {
		return new PlatformInfoResponse(applicationName, environment, version, "ready-for-foundation");
	}
}
package com.facundo.assistentia.application.system.dto;

public record PlatformInfoResponse(
	String applicationName,
	String environment,
	String version,
	String status
) {
}
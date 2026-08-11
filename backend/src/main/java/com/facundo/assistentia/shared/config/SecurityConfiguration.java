package com.facundo.assistentia.shared.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfiguration {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/actuator/health",
					"/actuator/info",
					"/api/v1/health",
					"/api/v1/system/info",
					"/api/v1/demo/quick-start",
					"/api/v1/workspaces",
					"/api/v1/workspaces/**",
					"/api/v1/teams",
					"/api/v1/users"
				).permitAll()
				.anyRequest().permitAll())
			.build();
	}
}
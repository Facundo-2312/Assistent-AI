package com.facundo.assistentia.shared.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfiguration {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/actuator/health",
					"/actuator/info",
					"/api/v1/health",
					"/api/v1/workspaces",
					"/api/v1/workspaces/join"
				).permitAll()
				.requestMatchers(HttpMethod.POST,
					"/api/v1/auth/login",
					"/api/v1/auth/refresh",
					"/api/v1/auth/logout"
				).permitAll()
				.requestMatchers(HttpMethod.POST, "/api/v1/users").hasRole("ADMIN")
				.requestMatchers(HttpMethod.POST, "/api/v1/teams").hasRole("ADMIN")
				.anyRequest().authenticated())
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}
}

package com.employee.manage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {
	
	private final JwtAuthFilter jwtAuthFilter;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/api/auth/**").permitAll()
	           // .requestMatchers("/api/employee/**").permitAll()  // FIXED
	            .anyRequest().authenticated()
	        )
	        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
	        .oauth2Login(oAuth2 -> oAuth2.failureHandler(
	        		(request, response, exception) -> {
	                    log.error("OAuth2 login failed: {}", exception.getMessage());
	                })
	        		.successHandler(oAuth2SuccessHandler)
	        		);

	    return http.build();
	}

	
}

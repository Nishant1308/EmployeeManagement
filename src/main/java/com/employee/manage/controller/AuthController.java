package com.employee.manage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.manage.dto.LoginRequestDto;
import com.employee.manage.dto.LoginResponseDto;
import com.employee.manage.dto.SignUpResponseDto;
import com.employee.manage.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
		LoginResponseDto response = authService.login(request);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<SignUpResponseDto> signUp(@RequestBody LoginRequestDto request) {
		SignUpResponseDto response = authService.signUp(request);
		return ResponseEntity.ok(response);
	}
}

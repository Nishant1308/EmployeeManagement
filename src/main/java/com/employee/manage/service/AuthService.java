package com.employee.manage.service;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.employee.manage.AuthUtil;
import com.employee.manage.dto.AuthProviderType;
import com.employee.manage.dto.LoginRequestDto;
import com.employee.manage.dto.LoginResponseDto;
import com.employee.manage.dto.RoleType;
import com.employee.manage.dto.SignUpResponseDto;
import com.employee.manage.entity.User;
import com.employee.manage.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final AuthenticationManager authenticationManager;
	private final AuthUtil authUtil;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public LoginResponseDto login(LoginRequestDto request) {

		
	Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getUsername(), request.getPassword()));
	
	User user = (User) authentication.getPrincipal();
	String token = authUtil.generateAccessToken(user);
	
	return new LoginResponseDto(token, user.getUsername());
		
	}
	
	public User signupInternal(LoginRequestDto request, AuthProviderType authProviderType, String providerId) {
		User user = userRepository.findByUsername(request.getUsername()).orElse(null);

		if (user != null) {
			throw new RuntimeException("Username already exists");
		}
		user =  User.builder()
				.username(request.getUsername())
				.providerId(providerId)
				.providerType(authProviderType)
				.roles(Set.of(RoleType.USER))
				.build();
		
		if(authProviderType==AuthProviderType.EMAIL) {
			user.setPassword(passwordEncoder.encode(request.getPassword()));
			
		}
		return userRepository.save(user);
	}

	public SignUpResponseDto signUp(LoginRequestDto request) {
		User user = signupInternal(request, AuthProviderType.EMAIL, null);
		
		return new SignUpResponseDto(user.getUsername());
	}

	@Transactional
	public ResponseEntity<LoginResponseDto> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {
	
		AuthProviderType providerType = authUtil.getProviderTypeFromRegistrationId(registrationId);
		String providerId = authUtil.determineProviderIdFromOAuth2User(oAuth2User, registrationId);
		
		User user = userRepository.findByProviderIdAndProviderType(providerId, providerType).orElse(null);
		
		String email = oAuth2User.getAttribute("email");
		
		User userEmail = userRepository.findByUsername(email).orElse(null);
		
		if(user==null && userEmail==null) {
			String username = authUtil.determinUsernameFromOAuth2User(oAuth2User, registrationId, providerId);
			
			user = signupInternal(new LoginRequestDto(username, null),providerType, providerId);
		}else if(user!=null) {
			if(email != null && !email.isBlank() && !email.equals(user.getUsername())) {
				user.setUsername(email);
				userRepository.save(user);
			}
		}else {
			throw new BadCredentialsException("This email is already registrated with the provider: "+userEmail.getProviderId());
		}
		
		LoginResponseDto loginResponseDto = new LoginResponseDto(authUtil.generateAccessToken(user), user.getUsername());
		
		return ResponseEntity.ok(loginResponseDto);
	}

}

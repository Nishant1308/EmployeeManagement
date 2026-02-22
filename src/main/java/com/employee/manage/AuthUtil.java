package com.employee.manage;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.employee.manage.dto.AuthProviderType;
import com.employee.manage.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class AuthUtil {

	@Value("${jwt.secret.key}")
	private String jwtSecretKey;
	
	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
	}
	
	public String generateAccessToken(User user) {
		return Jwts.builder()
				.subject(user.getUsername())
				.claim("userId", user.getId().toString())
				.issuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600000))  //1 hour
				.signWith(getSecretKey())
				.compact();
	}
	
	public String getUsernameFromToken(String token) {
		Claims claim = Jwts.parser()
				.verifyWith(getSecretKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
				
		return claim.getSubject();
	}
	
	public AuthProviderType getProviderTypeFromRegistrationId(String registrationId) {
		return switch (registrationId.toLowerCase()) {
		case "google" -> AuthProviderType.GOOGLE;
		case "facebook" -> AuthProviderType.FACEBOOK;
		case "github" -> AuthProviderType.GITHUB;
		
		default -> throw new IllegalArgumentException("Unnsupported oAuth2 Provider :"+registrationId);
		};
	}
	
	public String determineProviderIdFromOAuth2User(OAuth2User oauth2User, String registrationId) {
		String providerId = switch(registrationId.toLowerCase()) {
		case "google"->oauth2User.getAttribute("sub");
		case "github"->oauth2User.getAttribute("id").toString();
		default ->{
			throw new IllegalArgumentException("Unsupported OAuth2 Provider: "+registrationId);
		}
		};
		if(providerId == null || providerId.isBlank()) {
			throw new IllegalArgumentException("Unable to determine providerId for Oauth2 login");
		}
		return providerId;
	}
	
	public String determinUsernameFromOAuth2User(OAuth2User oauth2User, String registrationId, String providerId) {
		String email = oauth2User.getAttribute("email");
		if(email != null && !email.isBlank()) {
			return email;
		}
		return switch(registrationId.toLowerCase()) {
		case "google"->oauth2User.getAttribute("sub");
		case "github"->oauth2User.getAttribute("login");
		default->providerId;
		};
	}
	
}

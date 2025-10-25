package com.manas.emailer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manas.emailer.dto.response.AuthResponseDTO;
import com.manas.emailer.service.AuthService;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("auth")
@AllArgsConstructor
@Validated
public class AuthController {
	
	AuthService authService;
	
	@GetMapping("google/callback")
	public ResponseEntity<AuthResponseDTO> callback(@NotBlank(message = "Access code should not be blank") @RequestParam("code") String code) {
		return ResponseEntity.ok(authService.handleGoogleCallback(code)); 
	}
	
	@GetMapping("generate-token")
	public ResponseEntity<AuthResponseDTO> generateTokenUsingRefreshToken(@NotBlank(message = "Refresh token should not be blank") @RequestParam("refresh-token") String refreshToken) {
		return ResponseEntity.ok(authService.validateRefreshToken(refreshToken)); 
	}
}

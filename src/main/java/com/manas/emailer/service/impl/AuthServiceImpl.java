package com.manas.emailer.service.impl;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.manas.emailer.dto.response.AuthResponseDTO;
import com.manas.emailer.entity.User;
import com.manas.emailer.enums.TokenType;
import com.manas.emailer.exception.InvalidTokenException;
import com.manas.emailer.service.AuthService;
import com.manas.emailer.service.UserService;
import com.manas.emailer.util.JwtUtil;
import com.manas.emailer.util.TokenEncryptor;
import com.manas.emailer.util.TokenManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

	private final Environment env;

	private final UserService userService;

	private final WebClient webClient;

	private final JwtUtil jwtUtil;

	@Override
	public AuthResponseDTO handleGoogleCallback(String authorizationCode) {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("code", authorizationCode);
		params.add("client_id", env.getProperty("google.clientId"));
		params.add("client_secret", env.getProperty("google.clientSecret"));
		params.add("redirect_uri", env.getProperty("google.redirectUrl"));
		params.add("grant_type", "authorization_code");

		var tokenResponse = webClient.post()
				.uri("https://oauth2.googleapis.com/token")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData(params))
				.retrieve()
				.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (err) ->{
					return err.createException();
				})
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
				.block();
		if (tokenResponse == null) {
			throw new InvalidTokenException("Failed to get token response from Google OAuth server");
		}
		try {
			User user = getUserFromGoogle(tokenResponse);
			return buildAuthDTOWithJwt(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public User getUserFromGoogle(Map<String,Object> tokenResponse) throws Exception {

		System.out.println(tokenResponse);
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
				new NetHttpTransport(),
				new GsonFactory()
				)
				.setAudience(Collections.singletonList(env.getProperty("google.clientId") + ".apps.googleusercontent.com"))
				.build();

		GoogleIdToken idToken = verifier.verify(tokenResponse.get("id_token").toString());
		if (idToken != null) {
			GoogleIdToken.Payload payload = idToken.getPayload();
			TokenManager.saveToken(payload.getEmail(), tokenResponse.get("access_token").toString());
			System.out.println();
			return userService.saveUser(payload.get("name").toString(), payload.getEmail(), payload.get("picture").toString(), TokenEncryptor.encrypt(tokenResponse.get("refresh_token").toString()));
		}
		return null;
	}

	@Override
	public AuthResponseDTO buildAuthDTOWithJwt(User user) {

		return AuthResponseDTO.from(user, jwtUtil.generateToken(user.getEmail(), TokenType.ACCESS_TOKEN), jwtUtil.generateToken(user.getEmail(), TokenType.REFRESH_TOKEN), 5000L);
	}

	@Override
	public AuthResponseDTO validateRefreshToken(String refreshToken) {
		var user = userService.getUserByEmail(jwtUtil.extractEmail(refreshToken));

		if(user.isPresent() && jwtUtil.validateToken(refreshToken, user.get().getEmail(), TokenType.REFRESH_TOKEN)) {
			return buildAuthDTOWithJwt(user.get());
		}
		throw new InvalidTokenException("Invalid refresh token");
	}

	@Override
	public void getAccessTokenUsingRefreshToken(String email) throws Exception {
		Optional<User> userOp = userService.getUserByEmail(email);
		if(userOp.isPresent()) {
			User user = userOp.get();
			webClient.post()
			.uri("https://oauth2.googleapis.com/token")
			.header("Content-Type", "application/x-www-form-urlencoded")
			.bodyValue("client_id=" + env.getProperty("google.clientId") + ".apps.googleusercontent.com"  +
					"&client_secret=" + env.getProperty("google.clientSecret") +
					"&refresh_token=" + TokenEncryptor.decrypt(user.getGoogleRefreshToken())  +
					"&grant_type=refresh_token")
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
			.map(value -> {
				TokenManager.saveToken(email, value.get("access_token").toString());
				return value.get("access_token").toString();
			})
			.block();
		}
	}
}

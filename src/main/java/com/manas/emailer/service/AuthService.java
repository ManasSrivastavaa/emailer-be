package com.manas.emailer.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import com.manas.emailer.dto.response.AuthResponseDTO;
import com.manas.emailer.entity.User;

public interface AuthService {

    AuthResponseDTO handleGoogleCallback(String authorizationCode);
    
	User getUserFromGoogle(Map<String,Object> tokenResponse) throws GeneralSecurityException, IOException, Exception;
	
	AuthResponseDTO buildAuthDTOWithJwt(User user);

	AuthResponseDTO validateRefreshToken(String refreshToken);

	void getAccessTokenUsingRefreshToken(String email) throws Exception;
}

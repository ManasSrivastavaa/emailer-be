package com.manas.emailer.dto.response;

import com.manas.emailer.entity.User;

public record AuthResponseDTO(String name, String email, String profilePicUrl, String accessToken,String refreshToken,Long expiresIn)
{
	public static AuthResponseDTO from(User user, String accessToken, String refreshToken, Long expiresIn) {
		return new AuthResponseDTO(user.getName(),user.getEmail(),user.getProfilePictureURL(),accessToken,refreshToken,expiresIn);
	}
}


package com.manas.emailer.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.manas.emailer.entity.User;

public interface UserService extends UserDetailsService {
	
	User saveUser(String name, String email, String profilePicURL,String refreshToken);
	
	Optional<User> getUserByEmail(String emailId);
	
	User updateUser(User user);
}

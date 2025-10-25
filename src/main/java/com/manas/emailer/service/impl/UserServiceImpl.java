package com.manas.emailer.service.impl;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.manas.emailer.entity.User;
import com.manas.emailer.repository.UserRepository;
import com.manas.emailer.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;

	@Override
	public User saveUser(String name, String email, String profilePicURL, String refreshToken) {
		
		User user = userRepository.findByEmail(email).orElseGet(User::new); 
		user.setEmail(email);
		user.setName(name);
		user.setProfilePictureURL(profilePicURL);
		user.setGoogleRefreshToken(refreshToken);
		return userRepository.save(user);
	}

	@Override
	public Optional<User> getUserByEmail(String emailId) {
		return userRepository.findByEmail(emailId);
	}

	@Override
	public User updateUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		var userOp = this.getUserByEmail(email);
		if(userOp.isPresent()) {
			return userOp.get();
		}
		throw new UsernameNotFoundException("User not found by email :-" + email);
	}	
}

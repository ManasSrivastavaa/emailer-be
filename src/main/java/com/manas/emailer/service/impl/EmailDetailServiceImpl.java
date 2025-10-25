package com.manas.emailer.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.manas.emailer.entity.EmailDetails;
import com.manas.emailer.repository.EmailDetailRepository;
import com.manas.emailer.service.EmailDetailService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EmailDetailServiceImpl implements EmailDetailService{

	private final EmailDetailRepository emailDetailRepository;
	
	@Override
	public EmailDetails saveEmailDetail(String email,boolean isActive) {
	   return emailDetailRepository.save(new EmailDetails(email,isActive));
	}

	@Override
	public List<String> getEmailsByCompany(String searchValue) {
		return emailDetailRepository.getEmailsByCompany(searchValue);
	}

	@Override
	public List<String> getAllActiveEmails() {
		return emailDetailRepository.findAllByActive();
	}
	

}

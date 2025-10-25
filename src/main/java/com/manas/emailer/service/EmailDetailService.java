package com.manas.emailer.service;

import java.util.List;

import com.manas.emailer.entity.EmailDetails;

public interface EmailDetailService {

	public EmailDetails saveEmailDetail(String email, boolean isActive); 
	
	public List<String> getEmailsByCompany(String searchValue);
	
	public List<String> getAllActiveEmails();
}

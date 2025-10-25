package com.manas.emailer.service;

import com.manas.emailer.dto.request.EmailRequestDTO;
import com.manas.emailer.entity.EmailTemplate;

public interface EmailerService {

	String sendEmail(EmailRequestDTO emailrequest);

	String constructEmail(String from, String to, EmailTemplate emailTemplate);
	
}

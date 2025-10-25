package com.manas.emailer.service;

import java.util.List;

import com.manas.emailer.dto.request.TemplateRequestDTO;
import com.manas.emailer.entity.EmailTemplate;

public interface EmailTemplateService {

	public List<EmailTemplate> getMyEmailTemplate();
	
	public EmailTemplate saveEmailTemplate(TemplateRequestDTO emailTemplate);
	
	public EmailTemplate updateTemplate(Long templateId, TemplateRequestDTO emailTemplate);
	
	public EmailTemplate getById(Long id);
	
	public List<EmailTemplate> getByKeyword(String keyword);
	
	EmailTemplate getUserDefaultTemplate(Long userId);
	
	public EmailTemplate buyTemplate(Long templateId);
		
}

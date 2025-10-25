package com.manas.emailer.events.handler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.manas.emailer.dto.request.EmailRequestDTO;
import com.manas.emailer.events.TemplateSavedOrUpdateEvent;
import com.manas.emailer.service.EmailerService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TemplateEventHndler {
	
	private final EmailerService emailerService;
	
	@EventListener
	public void templateSavedOrUpdateEventHandler(TemplateSavedOrUpdateEvent event) {
		var emailTemplate = event.getEmailTemplate();
		var email = emailTemplate.getUser().getEmail();
		emailerService.sendEmail(new EmailRequestDTO(emailTemplate.getId(), email, email));
	}

}

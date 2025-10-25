package com.manas.emailer.events;

import org.springframework.context.ApplicationEvent;

import com.manas.emailer.entity.EmailTemplate;

import lombok.Getter;

@Getter
public class TemplateSavedOrUpdateEvent  extends ApplicationEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private EmailTemplate emailTemplate;

	public TemplateSavedOrUpdateEvent(EmailTemplate emailTemplate) {
		super(emailTemplate);
		this.emailTemplate = emailTemplate;
	}

}

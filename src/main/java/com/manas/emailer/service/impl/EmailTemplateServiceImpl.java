package com.manas.emailer.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.manas.emailer.dto.request.TemplateRequestDTO;
import com.manas.emailer.entity.Attachment;
import com.manas.emailer.entity.EmailTemplate;
import com.manas.emailer.entity.TemplatePurchase;
import com.manas.emailer.entity.User;
import com.manas.emailer.events.TemplateSavedOrUpdateEvent;
import com.manas.emailer.repository.EmailTemplateRepository;
import com.manas.emailer.service.EmailTemplateService;
import com.manas.emailer.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService{

	private final EmailTemplateRepository emailTemplateRepository;
	
	private final UserService userService;

	private final ApplicationEventPublisher eventPublisher;

	@Override
	public List<EmailTemplate> getMyEmailTemplate() {
		User user = ( User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		var emailTemplates =  emailTemplateRepository.findAllByUser_email(user.getEmail());
		emailTemplates.addAll(user.getPurchases().stream().map(purchase -> purchase.getEmailTemplate()).collect(Collectors.toList()));
		return emailTemplates;
	}

	@Override
	public EmailTemplate saveEmailTemplate(TemplateRequestDTO emailTemplateDTO) {

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EmailTemplate emailTemplate = new EmailTemplate(emailTemplateDTO, user);
		if(emailTemplate.isDefault()) {
			var defaultTemplate = this.getUserDefaultTemplate(user.getId());
			if(defaultTemplate != null) {
				defaultTemplate.setDefault(false);
				defaultTemplate = emailTemplateRepository.save(defaultTemplate);
			}
		}
		emailTemplate = emailTemplateRepository.save(emailTemplate);
		eventPublisher.publishEvent(new TemplateSavedOrUpdateEvent(emailTemplate));
		return emailTemplate;
		
	}

	@Override
	public EmailTemplate getById(Long id) {
		return emailTemplateRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Email template not found with id: " + id));
	}

	@Override
	public EmailTemplate updateTemplate(Long templateId, TemplateRequestDTO emailTemplate) {
		var template = this.getById(templateId);
		if(emailTemplate.attachment() != null) {
			template.setAttachment(new Attachment(emailTemplate.attachment()));
		}else {
			template.setAttachment(null);
		}
		template.setBody(emailTemplate.body());
		template.setDefault(emailTemplate.isDefault());
		template.setDomainsToExclude(emailTemplate.domainsToExclude());
		template.setSubject(emailTemplate.subject());
		template.setPublic(emailTemplate.isPublic());
		template.setPrice(emailTemplate.price());
		template.setDefault(emailTemplate.isDefault());
		if(emailTemplate.isDefault()) {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			var defaultTemplate = this.getUserDefaultTemplate(user.getId());
			if(defaultTemplate != null) {
				defaultTemplate.setDefault(false);
				defaultTemplate = emailTemplateRepository.save(defaultTemplate);
			}
		}
		return emailTemplateRepository.save(template);
	}

	@Override
	public List<EmailTemplate> getByKeyword(String keyword) {
		User user = ( User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return emailTemplateRepository.searchPublicTemplates(keyword, user.getId() ,PageRequest.of(0, 10));
	}

	@Override
	public EmailTemplate getUserDefaultTemplate(Long userId) {
		return emailTemplateRepository.getDefaultTemplateByUserId(userId);
	}

	@Override
	public EmailTemplate buyTemplate(Long templateId) {
		EmailTemplate emailTemplate = this.getById(templateId);
		User user = ( User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		var templatePurchase = new TemplatePurchase(user,emailTemplate);
		var allPurchasedTemplate = user.getPurchases();
		allPurchasedTemplate.add(templatePurchase);
		user.setPurchases(allPurchasedTemplate);
		userService.updateUser(user);
		return emailTemplateRepository.save(emailTemplate);
	}
}

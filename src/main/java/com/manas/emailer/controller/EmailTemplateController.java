package com.manas.emailer.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manas.emailer.dto.request.TemplateRequestDTO;
import com.manas.emailer.entity.EmailTemplate;
import com.manas.emailer.service.EmailTemplateService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/email-template")
@Validated
public class EmailTemplateController {
	
    private final EmailTemplateService emailTemplateService;
	
	@GetMapping()
	public ResponseEntity<List<EmailTemplate>> getAllTemplates(@NotBlank(message = "EmailId should not be blank") @RequestParam(required = true) String emailId){
		return ResponseEntity.ok(emailTemplateService.getMyEmailTemplate());
	}
	
	@PostMapping()
	public ResponseEntity<EmailTemplate> saveEmailTemplate(@Valid @RequestBody TemplateRequestDTO emailTemplateDTO){
		return ResponseEntity.ok(emailTemplateService.saveEmailTemplate(emailTemplateDTO));
	}
	
	@PutMapping("/{templateId}")
	public ResponseEntity<EmailTemplate> updateEmailTemplate(@PathVariable Long templateId,@Valid @RequestBody TemplateRequestDTO emailTemplateDTO){
		return ResponseEntity.ok(emailTemplateService.updateTemplate(templateId,emailTemplateDTO));
	}
	
	@GetMapping("search")
	public ResponseEntity<List<EmailTemplate>> searchByKeyWord(@NotBlank(message = "KeyWord should not be blank") @RequestParam(required = true) String keyword){
		return ResponseEntity.ok(emailTemplateService.getByKeyword(keyword));
	}
	
	@PostMapping("buy/{templateId}")
	public ResponseEntity<EmailTemplate> buyEmailTemplate(@PathVariable Long templateId){
		return ResponseEntity.ok(emailTemplateService.buyTemplate(templateId));
	}
}

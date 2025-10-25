 package com.manas.emailer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manas.emailer.dto.request.EmailRequestDTO;
import com.manas.emailer.service.EmailerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email/")
public class EmailerController {
	
	private final EmailerService emailerService;
	
	@PostMapping("send")
	public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequestDTO emailrequest) {
		emailerService.sendEmail(emailrequest);
		return ResponseEntity.ok("Sending Mail");
	}
	
}


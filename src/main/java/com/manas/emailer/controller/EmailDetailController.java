package com.manas.emailer.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manas.emailer.service.EmailDetailService;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/api/email-detail")
@AllArgsConstructor
@Validated
public class EmailDetailController {
	
	private final EmailDetailService emailDetailService;
	
	@GetMapping
	public List<String> getEmailDetails(@NotBlank(message = "Email id should not be blank") @RequestParam(required = true) String searchValue){
		return emailDetailService.getEmailsByCompany(searchValue);
	}

}

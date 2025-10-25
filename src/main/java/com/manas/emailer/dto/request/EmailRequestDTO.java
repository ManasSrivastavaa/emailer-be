package com.manas.emailer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record EmailRequestDTO(
        @NotNull(message = "Template ID cannot be null")
        @Positive(message = "Template ID must be a positive number")
        Long templateId,

        @NotBlank(message = "Recipient email cannot be blank")
        String toEmail,

        @NotBlank(message = "Sender email cannot be blank")
        @Email(message = "Sender email should be valid")
        String fromEmail
) {}
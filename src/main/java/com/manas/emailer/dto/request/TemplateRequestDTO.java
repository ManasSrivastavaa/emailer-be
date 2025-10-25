package com.manas.emailer.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record TemplateRequestDTO(
	@NotBlank(message = "Subject cannot be blank")	
    String subject,
    @NotBlank(message = "Body cannot be blank")
    String body,
    @Valid
    AttachmentDTO attachment,
    @NotBlank(message = "Role applying cannot be blank")
    String roleApplying,
    boolean isPublic,
    String domainsToExclude,
    int price,
    boolean isDefault
) {}



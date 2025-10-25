package com.manas.emailer.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AttachmentDTO(
		@NotEmpty(message = "File name cannot be blank")
		String fileName,
		@NotNull(message = "File content cannot be null")
        @Size(min = 1, message = "File content cannot be empty")
		byte[] data,
		@NotEmpty(message = "File type cannot be blank")
		String mimeType) {
}

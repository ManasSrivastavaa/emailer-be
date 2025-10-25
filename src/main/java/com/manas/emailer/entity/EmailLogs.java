package com.manas.emailer.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class EmailLogs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "send_date", nullable = false)
	private ZonedDateTime sendDate;

	@Column(name = "is_successful", nullable = false)
	private boolean isSuccessful;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sent_to")
	private EmailDetails emailDetails;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sent_by")
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "template_used")
	private EmailTemplate templateUsed;

	@Column(name = "fail_reason")
	private String failReason;

	public EmailLogs(boolean isSuccessful, EmailDetails emailDetails, User user, String failReason, EmailTemplate templateUsed) {
		this.sendDate = ZonedDateTime.now(ZoneId.of("UTC"));
		this.isSuccessful = isSuccessful;
		this.emailDetails = emailDetails;
		this.user = user;
		this.failReason = failReason;
		this.templateUsed = templateUsed;
	}


}

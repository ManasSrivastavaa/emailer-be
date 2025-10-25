package com.manas.emailer.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.manas.emailer.entity.embeddable.AuditDetails;
import com.manas.emailer.entity.embeddable.UserTemplateId;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Data;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Ratings {

	@EmbeddedId
	private UserTemplateId id;

	@MapsId("userId")  
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User ratedBy;

	@MapsId("templateId") 
	@ManyToOne
	@JoinColumn(name = "email_template_id")
	private EmailTemplate emailTemplate;	
	
	private int rating;
	
	@Embedded
	private AuditDetails auditDetails;



}

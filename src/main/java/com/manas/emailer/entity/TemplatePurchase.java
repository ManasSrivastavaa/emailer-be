package com.manas.emailer.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.manas.emailer.entity.embeddable.UserTemplateId;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TemplatePurchase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private UserTemplateId id;

	@MapsId("userId")  
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@MapsId("templateId") 
	@ManyToOne
	@JoinColumn(name = "template_id")
	private EmailTemplate emailTemplate;

	@CreatedDate
	private ZonedDateTime purchaseDate;

	private String paymentMethod;

	public TemplatePurchase(User buyer,EmailTemplate template) {

		this.purchaseDate = ZonedDateTime.now(ZoneId.of("UTC"));
		this.paymentMethod = "Testing";
	}

}

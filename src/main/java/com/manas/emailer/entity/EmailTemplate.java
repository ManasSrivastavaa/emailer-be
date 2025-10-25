package com.manas.emailer.entity;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manas.emailer.dto.request.TemplateRequestDTO;
import com.manas.emailer.entity.embeddable.AuditDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "email_templates")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class EmailTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false)
    @Nationalized
    private String body;

    @OneToOne(cascade = CascadeType.ALL,fetch =FetchType.EAGER)
    @JoinColumn(name = "attachment_id", referencedColumnName = "id")
    private Attachment attachment;

    @Column(name = "domains_to_exclude", length = 1000)
    private String domainsToExclude;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
     
    private boolean isDefault = false;
    
    private boolean isPublic = false;
    
    private String roleApplying;
    
    private Long totalUsedBy = 0L;
    
    private AuditDetails auditDetail;
    
    private int price = 10;
   
   public EmailTemplate(TemplateRequestDTO emailTemplateDTO,User user) {
    	this.subject = emailTemplateDTO.subject();
    	this.body = emailTemplateDTO.body();
    	this.domainsToExclude = emailTemplateDTO.domainsToExclude();
    	if(emailTemplateDTO.attachment() != null) {
           this.attachment = new Attachment(emailTemplateDTO.attachment());
    	}
    	this.roleApplying = emailTemplateDTO.roleApplying();
    	this.isPublic = emailTemplateDTO.isPublic();
    	this.user = user;
    	this.isDefault = emailTemplateDTO.isDefault();
    	this.price = emailTemplateDTO.price();
    }
}

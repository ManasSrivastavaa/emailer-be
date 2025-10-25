package com.manas.emailer.entity;

import java.text.Normalizer;
import java.util.List;
import java.util.Objects;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.manas.emailer.entity.embeddable.AuditDetails;
import com.manas.emailer.util.EmailDataExtracter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "email_details")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class EmailDetails {

    @Id
    @Column(name = "hr_email", nullable = false, unique = true)
    private String hrEmail;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;   
    
    private String hiresFor; 
    
    @Embedded
    private AuditDetails auditDetails;
    
    private String company;
    
    private String country;
    
    private String name;
    
    @OneToMany(mappedBy = "emailDetails",cascade = CascadeType.ALL)
    private List<EmailLogs> logs;
    
    public EmailDetails(String email,boolean isActive) {
    	email = Normalizer.normalize(email, Normalizer.Form.NFKD).replaceAll("\\p{M}", "");
    	this.hrEmail = email;
    	this.active = isActive;
    	this.company = EmailDataExtracter.extractCompany(email);
    }
    

	@Override
	public int hashCode() {
		return Objects.hash(this.hrEmail.toLowerCase());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmailDetails other = (EmailDetails) obj;
		return  Objects.equals(hrEmail.toLowerCase(), other.hrEmail.toLowerCase());	
	}
}

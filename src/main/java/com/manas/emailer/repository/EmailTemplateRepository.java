package com.manas.emailer.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.manas.emailer.entity.EmailTemplate;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long>{
	
	List<EmailTemplate> findAllByUser_email(String emailId);
	
	@Query("SELECT t FROM EmailTemplate t WHERE t.isPublic = true AND t.user.id != :userId  AND (t.roleApplying LIKE CONCAT('%', :keyword, '%') OR t.subject LIKE CONCAT('%', :keyword, '%') OR t.body LIKE CONCAT('%', :keyword, '%'))")
	List<EmailTemplate> searchPublicTemplates(@Param("keyword") String keyword,Long userId ,Pageable pageable);
	
	@Query("SELECT t FROM EmailTemplate t WHERE t.isDefault = true AND t.user.id = :userId")
	EmailTemplate getDefaultTemplateByUserId(Long userId);

}



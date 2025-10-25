package com.manas.emailer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manas.emailer.entity.EmailDetails;

public interface EmailDetailRepository extends JpaRepository<EmailDetails, String>{
	
	@Query("SELECT ed.hrEmail FROM EmailDetails ed " +
		       "WHERE ed.active = true AND ed.hrEmail LIKE CONCAT('%', :searchValue, '%')")
	List<String> getEmailsByCompany(String searchValue);
	
	@Query("SELECT ed.hrEmail from EmailDetails ed where ed.active = true")
	List<String> findAllByActive();

}

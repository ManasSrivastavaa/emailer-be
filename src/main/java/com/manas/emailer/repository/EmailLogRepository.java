package com.manas.emailer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manas.emailer.entity.EmailLogs;

public interface EmailLogRepository extends JpaRepository<EmailLogs, Long> {
}

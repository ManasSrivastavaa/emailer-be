package com.manas.emailer.service.impl;

import org.springframework.stereotype.Service;

import com.manas.emailer.entity.EmailLogs;
import com.manas.emailer.repository.EmailLogRepository;
import com.manas.emailer.service.EmailerLogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailLogServiceImpl implements EmailerLogService {
	
	private final EmailLogRepository emailLogRepository;

	@Override
	public void saveLog(EmailLogs emailLog) {
		emailLogRepository.save(emailLog);
	}
	 
}

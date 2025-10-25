package com.manas.emailer.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.manas.emailer.dto.request.EmailRequestDTO;
import com.manas.emailer.entity.EmailLogs;
import com.manas.emailer.entity.EmailTemplate;
import com.manas.emailer.entity.User;
import com.manas.emailer.service.AuthService;
import com.manas.emailer.service.EmailDetailService;
import com.manas.emailer.service.EmailTemplateService;
import com.manas.emailer.service.EmailerLogService;
import com.manas.emailer.service.EmailerService;
import com.manas.emailer.util.TokenManager;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EmailerServiceImpl implements EmailerService {

	private final WebClient webClient;

	private final EmailDetailService emailDetailService;

	private final EmailTemplateService emailTemplateService;

	private final AuthService authService;

	private final EmailerLogService emailerLogService;

	@Override
	public String sendEmail(EmailRequestDTO emailrequestDTO) {

		EmailTemplate emailTemplate = emailTemplateService.getById(emailrequestDTO.templateId());
		List<String> emails = new ArrayList<String>();
		emails.addAll(Arrays.asList(emailrequestDTO.toEmail().split(",")));
		if(emails.contains("all@all.com")) {
			emails.addAll(emailDetailService.getAllActiveEmails());
		}
		
		for(String email : emails) {
			if(email.endsWith(emailTemplate.getDomainsToExclude())) {
				continue;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String rawEmail = constructEmail(emailrequestDTO.fromEmail(),email,emailTemplate);
			Map<String, Object> requestBody = Map.of("raw", rawEmail);
			if(TokenManager.getToken(emailrequestDTO.fromEmail()) == null){
				try {
					authService.getAccessTokenUsingRefreshToken(user.getEmail());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Mono<Object> response = webClient
					.post()
					.uri("https://gmail.googleapis.com/gmail/v1/users/me/messages/send")
					.bodyValue(requestBody)
					.headers(header -> {
						header.setBearerAuth(TokenManager.getToken(emailrequestDTO.fromEmail()));          
						header.setContentType(MediaType.APPLICATION_JSON)
						;})
					.retrieve()
					.bodyToMono(Object.class);
			response.subscribe(
					result ->{
						var ed = emailDetailService.saveEmailDetail(email,true);
						emailerLogService.saveLog(new EmailLogs(true, ed, user, "",emailTemplate));

					},
					error ->{
						var ed = emailDetailService.saveEmailDetail(email,true);
						if(error.getMessage().length()>1000) {
							emailerLogService.saveLog(new EmailLogs(false, ed, user, error.getMessage().substring(0, 1000),emailTemplate));
						}else
							emailerLogService.saveLog(new EmailLogs(false, ed, user, error.getMessage(),emailTemplate));
					}
					);
		}
		return "Sending Mails";
	}

	@Override
	public String constructEmail(String from, String to ,EmailTemplate emailTemplate) {
		String subject = emailTemplate.getSubject();
		String body = emailTemplate.getBody();
		String boundary = "boundary123";

		String raw =  "From: " + from + "\r\n" +
				"To: " + to + "\r\n" +
				"Subject: " + subject + "\r\n" +
				"MIME-Version: 1.0\r\n" +
				"X-Priority: 1\r\n" +           
				"Importance: High\r\n" +         
				"Priority: Urgent\r\n" +         
				"Content-Type: multipart/mixed; boundary=" + boundary + "\r\n\r\n"  
				//body
				+ "--" + boundary + "\r\n"
				+ "Content-Type: text/html; charset=UTF-8\r\n\r\n"
				+ body + "\r\n\r\n";

		if(emailTemplate.getAttachment() != null) {

			byte[] attachmentBytes = emailTemplate.getAttachment().getData();
			String attachmentFileName = emailTemplate.getAttachment().getFileName();
			String mimeType = emailTemplate.getAttachment().getMimeType();
			String encodedAttachment = Base64.getMimeEncoder().encodeToString(attachmentBytes);

			raw = raw +  "--" + boundary + "\r\n"
					+ "Content-Type: " + mimeType + "; name=\"" + attachmentFileName + "\"\r\n"
					+ "Content-Disposition: attachment; filename=\"" + attachmentFileName + "\"\r\n"
					+ "Content-Transfer-Encoding: base64\r\n\r\n"
					+ encodedAttachment + "\r\n\r\n";
		}

		//end
		raw = raw + "--" + boundary + "--";

		return Base64.getUrlEncoder()
				.withoutPadding()
				.encodeToString(raw.getBytes(StandardCharsets.UTF_8));
	}

}

package com.manas.emailer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableJpaAuditing
//@RequiredArgsConstructor
public class EmailerApplication {

//	private final EmailDetailService emailDetailService;

	public static void main(String[] args) {
		SpringApplication.run(EmailerApplication.class, args);
	}
//	@EventListener(value = ApplicationReadyEvent.class)
//	public  void main2() {
//		System.out.println("Hi");
//		File input = new File("C:/Users/nv.com/Documents/Book1.xlsx");
//		try {
//			System.out.println("Helo");
//			Workbook wb = WorkbookFactory.create(input);
//			var sheet = wb.getSheetAt(0);
//			for (Row row : sheet) { 
//				Cell cell = row.getCell(0); 
//				System.out.println(cell);
//				if(cell != null && cell.getStringCellValue() != null && !cell.getStringCellValue().isBlank()) {
//					System.out.println(cell.getStringCellValue());
//					emailDetailService.saveEmailDetail(cell.getStringCellValue(), true);
//				}
//			}
//		}catch(Exception ex) {
//			ex.printStackTrace();
//		}
//	}

	@Bean
	WebClient webClient() {
		return WebClient.builder()
				.defaultHeader("Content-Type", "application/json")
				.defaultStatusHandler(HttpStatusCode::isError,
						response -> response.bodyToMono(String.class)
						.map(body -> new RuntimeException("Error: " + body))
						)
				.build();
	}

}

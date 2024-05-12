package com.mengen;

import com.mengen.email.EmailSenderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class OnlineFoodOrderingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineFoodOrderingAppApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void sendEmail() {

		EmailSenderService emailSenderService = new EmailSenderService();
		emailSenderService.sendEmail("metehan1007@gmail.com", "Test Subject", "Test Body");
	}

}

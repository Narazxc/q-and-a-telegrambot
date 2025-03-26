package com.example.telegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class TelegramBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramBotApplication.class, args);

		System.out.println("✅ Telegram bot is running...");
	}
}

// choose number to select module
// list all question related to that module WITH qanda code
// once user type the qanda code, send the answer of that qanda code
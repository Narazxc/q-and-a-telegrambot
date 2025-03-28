package com.example.telegrambot.config;

import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.telegrambot.service.TelegramBot;


@Configuration
public class BotConfig {

    private final TelegramBot telegramBot;
    private static final Logger logger = LoggerFactory.getLogger(BotConfig.class);

    public BotConfig(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

//    @Bean
//    public TelegramBotsApi telegramBotsApi(TelegramBot telegramBot) throws TelegramApiException {
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//        telegramBot.clearWebhook();  // Ensure webhook is cleared
//        telegramBotsApi.registerBot(telegramBot); // Register the bot
//        return telegramBotsApi;
//    }

//    @Async
//    @PostConstruct // Runs automatically when the application starts
//    public void startBot() {
//        try {
//            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//            telegramBotsApi.registerBot(telegramBot);
//            System.out.println("Telegram Bot started in a separate thread.");
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }

    @Async
    @PostConstruct
    public void startBot() {
        boolean connected = false;
        while (!connected) {
            try {
                TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                logger.info("Registering Telegram bot...");
                telegramBotsApi.registerBot(telegramBot);
                logger.info("Telegram bot started successfully!");
                System.out.println("Telegram Bot started in a separate thread.");
                connected = true;
            } catch (TelegramApiException e) {
                logger.error("Failed to connect to Telegram: {}", e.getMessage(), e);
                logger.warn("Retrying in 10 seconds...");
                System.err.println("Failed to connect to Telegram: " + e.getMessage());
                System.out.println("Retrying in 10 seconds...");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ie) {
                    logger.error("Bot reconnection thread interrupted!", ie);
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

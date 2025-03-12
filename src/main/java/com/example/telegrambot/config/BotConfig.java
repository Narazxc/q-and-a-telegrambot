package com.example.telegrambot.config;

import com.example.telegrambot.service.TelegramBot;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {

//    @Bean
//    public TelegramBotsApi telegramBotsApi(TelegramBot telegramBot) throws TelegramApiException {
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//        telegramBotsApi.registerBot(telegramBot); // Register the bot
//        return telegramBotsApi;
//    }

    private final TelegramBot telegramBot;

    public BotConfig(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
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
                telegramBotsApi.registerBot(telegramBot);
                System.out.println("Telegram Bot started in a separate thread.");
                connected = true;
            } catch (TelegramApiException e) {
                System.err.println("Failed to connect to Telegram: " + e.getMessage());
                System.out.println("Retrying in 10 seconds...");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

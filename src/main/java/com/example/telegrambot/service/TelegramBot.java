package com.example.telegrambot.service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.example.telegrambot.dto.QAndAResponseDTO;
import com.example.telegrambot.model.ModuleModel;


@Service
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);
    private final ModuleService moduleService;
    private final QAndAService qAndAService;

    public TelegramBot(ModuleService moduleService, QAndAService qAndAService) {
        this.moduleService = moduleService;
        this.qAndAService = qAndAService;
    }

    @Override
    public void onUpdateReceived(Update update) {

        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                handleTextMessage(update.getMessage());
            }
//            else if (update.hasCallbackQuery()) {
//                handleCallbackQuery(update.getCallbackQuery());
//            }
            // Handle other types of updates if needed
        } catch (Exception e) {
            logger.error("Error processing update: {}", e.getMessage(), e);
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    // handle normal text
    private void handleTextMessage(Message message) {
        String text = message.getText();
        String chatId = message.getChatId().toString();

        if (text.startsWith("/")) {
            handleCommand(chatId, text);
        }
        else {
            handleNonCommandMessage(chatId, text);
        }
    }

    // handle string that starts with /
    private  void handleCommand(String chatId, String messageText) {

        switch (messageText.toLowerCase()) {
            case "/menu":
                sendModuleList(chatId);
            break;
            default:
                sendMessage(chatId, "Unknown command");
            break;
        }

    }

    private void handleNonCommandMessage(String chatId, String text) {
        // Cache modules or load them less frequently
        List<ModuleModel> modules = moduleService.getAllModules();
        List<String> answerPrefixes = modules.stream()
                .map(ModuleModel::getName)
                .map(name -> name + "00")  // Append "00" to each element
                .toList();

        List<String> moduleOptions = modules.stream()
                .map(ModuleModel::getName)
                .toList();

        if (answerPrefixes.stream().anyMatch(prefix -> text.toLowerCase().startsWith(prefix.toLowerCase()))) {
//            sendAnswer(chatId, text);
            sendLongMessage(chatId, text);
        } else if (moduleOptions.stream().anyMatch(module -> text.toLowerCase().equals(module.toLowerCase()))) {
            sendQuestionsByModule(chatId, text);
        } else {
            sendMessage(chatId, "Unknown command, Type /menu to see available options.");
        }
    }


    public void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendModuleList(String chatId) {
        List<ModuleModel> modules = moduleService.getAllModules();

        if (modules.isEmpty()) {
            sendMessage(chatId, "No modules found!");
            return;
        }

        String moduleListMessage = modules.stream()
                .map(module -> "• " + module.getName()) // Use "• " instead of numbering
                .collect(Collectors.joining("\n"));

        sendMessage(chatId, "Available Modules:\n" + moduleListMessage);
    }

    private void sendQuestionsByModule(String chatId, String inputModule) {
        List<QAndAResponseDTO> qAndAModels = qAndAService.getQuestionsByModuleName(inputModule);

        if (qAndAModels.isEmpty()) {
            sendMessage(chatId, "No Q&A found for module: " + inputModule);
            return;
        }

            String questionsListMessage = qAndAModels.stream()
            .map(q -> "• " + "[" + q.questionCode() + "]  " + q.question()) // Use bullet point instead of numbering
            .collect(Collectors.joining("\n"));

        sendMessage(chatId, "សំណួរសម្រាប់មុខងារ " + inputModule.toUpperCase() + "៖\n" + questionsListMessage);
    }


    private void sendAnswer(String chatId, String textMessage) {
        String answer = qAndAService.getAnswerByQuestionCode(textMessage);
        String question = qAndAService.getQuestionByQuestionCode(textMessage);

        if (answer == "" && question == "") {
            sendMessage(chatId, "No answer found for the question with question code: " + textMessage);
            return;
        }

        String qAndA = MessageFormat.format(
                "*សំណួរ*: {0}\n\n*ចម្លេីយ*: {1}",
                escapeMarkdownV2(question),
                escapeMarkdownV2(answer)
        );

        sendMessageMarkDownV2(chatId, qAndA);
    }

    private void sendMessageMarkDownV2(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode("MarkdownV2"); // Use "MarkdownV2"
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Escape special characters for MarkdownV2
    private String escapeMarkdownV2(String text) {
        return text.replaceAll("([_*\\[\\]()~`>#+\\-=|{}.!])", "\\\\$1");
    }




    // Currently in use
    // Automatically split text into multiple message, when exceed maximum size of 4kb or 4096 character
//    public void sendLongMessage(String chatId, String textMessage) {
//        int limit = 4096; // Telegram's message limit
//        int start = 0;
//
//        String answer = qAndAService.getAnswerByQuestionCode(textMessage);
//        String question = qAndAService.getQuestionByQuestionCode(textMessage);
//
//        if (answer == "" && question == "") {
//            sendMessage(chatId, "No answer found for the question with question code: " + textMessage);
//            return;
//        }
//
//        String qAndA = MessageFormat.format(
//                "*សំណួរ*: {0}\n\n*ចម្លេីយ*: {1}",
//                escapeMarkdownV2(question),
//                escapeMarkdownV2(answer)
//        );
//
//
//        // here this part please fix this
//        while (start < message.length()) {
//            int end = Math.min(start + limit, message.length());
//
//            // Avoid splitting in the middle of a word
//            if (end < message.length()) {
//                int lastSpace = message.lastIndexOf(" ", end);
//                if (lastSpace > start) {
//                    end = lastSpace; // Move end to last space before limit
//                }
//            }
//
//            String chunk = message.substring(start, end);
//
//            SendMessage sendMessage = new SendMessage(chatId, chunk);
//            try {
//                execute(sendMessage);
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//
//            start = end + 1; // Move start to the next character after the last split
//        }
//    }
    public void sendLongMessage(String chatId, String textMessage) {
        int limit = 4096; // Telegram's message limit
        int start = 0;

        String answer = qAndAService.getAnswerByQuestionCode(textMessage);
        String question = qAndAService.getQuestionByQuestionCode(textMessage);

        if (answer.isEmpty() && question.isEmpty()) {
            sendMessage(chatId, "No answer found for the question with question code: " + textMessage);
            return;
        }

        String qAndA = MessageFormat.format(
                "*សំណួរ*: {0}\n\n*ចម្លេីយ*: {1}",
                escapeMarkdownV2(question),
                escapeMarkdownV2(answer)
        );

        // Fix: Use qAndA instead of message (which is undefined)
        while (start < qAndA.length()) {
            int end = Math.min(start + limit, qAndA.length());

            // Fix: Ensure we don't break MarkdownV2 formatting
            if (end < qAndA.length()) {
                int lastSpace = qAndA.lastIndexOf(" ", end);
                int lastNewLine = qAndA.lastIndexOf("\n", end);
                int splitPoint = Math.max(lastSpace, lastNewLine);

                if (splitPoint > start) {
                    end = splitPoint; // Move end to the last space/newline before limit
                }
            }

            String chunk = qAndA.substring(start, end);

            SendMessage sendMessage = new SendMessage(chatId, chunk);
            sendMessage.setParseMode("MarkdownV2"); // Preserve Markdown formatting

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            start = end + 1; // Move start to the next chunk
        }
    }


}

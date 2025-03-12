package com.example.telegrambot.service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.example.telegrambot.dto.QAndAResponseDTO;
import com.example.telegrambot.model.ModuleModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    private final ModuleService moduleService;
    private final QAndAService qAndAService;

    public TelegramBot(ModuleService moduleService, QAndAService qAndAService) {
        this.moduleService = moduleService;
        this.qAndAService = qAndAService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        List<ModuleModel> modules = moduleService.getAllModules();

        List<String> answerPrefixes = modules.stream()
                .map(ModuleModel::getName)
                .map(name -> name + "00")  // Append "00" to each element
                .toList();

        List<String> moduleOptions = modules.stream()
                .map(ModuleModel::getName)
                .toList();



        if (message != null && message.hasText()) {
            String messageText = message.getText();
            String chatId = message.getChatId().toString();

            //  If messageText not starting with / and starts with the name of existing module
            if (!messageText.startsWith("/") && answerPrefixes.stream()
                    .anyMatch(prefix -> messageText.toLowerCase().startsWith(prefix.toLowerCase()))) {
                sendAnswer(chatId, messageText);
                return;
            }

//            if (messageText.startsWith("00")) {
//                sendMessage(chatId, "You choose: " + messageText);
//                return;
//            }


            // for any message that not starts with / and starts with the name of existing module
            if (!messageText.startsWith("/") && moduleOptions.stream()
                    .anyMatch(prefix -> messageText.toLowerCase().startsWith(prefix.toLowerCase()))) {

                sendQuestionsByModule(chatId, messageText);
                return;
            }

            if (messageText.startsWith("/")) {
                switch (messageText.toLowerCase()) {
                    case "/menu":
                        sendModuleList(chatId);
                        break;

                    default:
                        sendMessage(chatId, "Unknown command");
                        break;
                }
            }
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

        // By id
//        String moduleListMessage = modules.stream()
//                .map(module -> module.getId() + ". " + module.getName())
//                .collect(Collectors.joining("\n"));
        // By index
//        String moduleListMessage = IntStream.range(0, modules.size())
//                .mapToObj(i -> (i + 1) + ". " + modules.get(i).getName())
//                .collect(Collectors.joining("\n"));
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

        // By index
//        String questionsListMessage = IntStream.range(0, qAndAModels.size())
//                .mapToObj(i -> (i + 1) + ". " + qAndAModels.get(i).question() + " " + qAndAModels.get(i).questionCode())
//                .collect(Collectors.joining("\n"));
        //===========================================
//        String questionsListMessage = qAndAModels.stream()
//                .map(q -> "• " + q.question() + " " + q.questionCode()) // Use bullet point instead of numbering
//                .collect(Collectors.joining("\n"));

                String questionsListMessage = qAndAModels.stream()
                .map(q -> "• " + "[" + q.questionCode() + "]  " + q.question()) // Use bullet point instead of numbering
                .collect(Collectors.joining("\n"));

        sendMessage(chatId, "សំណួរសម្រាប់មុខងារ " + inputModule.toUpperCase() + "៖\n" + questionsListMessage);
    }


    // Working, but not support bold text format
//    private void sendAnswer(String chatId, String textMessage) {
//        String answer = qAndAService.getAnswerByQuestionCode(textMessage);
//        sendMessage(chatId, answer);
//    }

    // Working but mark down v1
//    // markdownV1 (legacy) support simple text format such as bold text
//    private void sendAnswer(String chatId, String textMessage) {
//        String answer = qAndAService.getAnswerByQuestionCode(textMessage);
//        String question = qAndAService.getQuestionByQuestionCode(textMessage);
//
////        String qAndA = 'question + '\n' + answer'
//        // Using string format
//        String qAndA = MessageFormat.format("**សំណួរ**: {0} \n\n {1}", question, answer);
//
//        sendMessageMarkDownV1(chatId, qAndA);
//    }
//
//
////    ================================================================
//    public void sendMessageMarkDownV1(String chatId, String text) {
//        SendMessage message = new SendMessage();
//        message.setChatId(chatId);
//        message.setText(text);
//        message.setParseMode("Markdown"); // Set MarkdownV2 mode
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }


private void sendAnswer(String chatId, String textMessage) {
    String answer = qAndAService.getAnswerByQuestionCode(textMessage);
    String question = qAndAService.getQuestionByQuestionCode(textMessage);

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


}





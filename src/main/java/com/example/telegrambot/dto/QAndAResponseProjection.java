package com.example.telegrambot.dto;

import java.time.Instant;
import java.util.UUID;

public interface QAndAResponseProjection {
    UUID getId();
    String getQuestionCode();
    String getQuestion();
    String getAnswer();
    String getModule();
    UUID getModuleId();
    String getModuleFullName();
    String getImagePath();

    Instant getCreatedAt();
    Instant getUpdatedAt();
}

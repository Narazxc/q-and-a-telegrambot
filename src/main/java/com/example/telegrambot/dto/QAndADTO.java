package com.example.telegrambot.dto;

import java.util.UUID;

public record QAndADTO(
        String questionCode,
        String question,
        String answer,
        String fullName,
        UUID moduleId // Keep it as Long since it references an ID
) {
}

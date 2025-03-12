package com.example.telegrambot.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public record QAndAResponseDTO(
        UUID id,
        String questionCode,
        String question,
        String answer,
        String module,    // Module name
        UUID moduleId,     // Module ID
        String moduleFullName, // Add fullName here
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}

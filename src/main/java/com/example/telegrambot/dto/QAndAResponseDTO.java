package com.example.telegrambot.dto;

import java.util.UUID;
import java.time.OffsetDateTime;

public record QAndAResponseDTO(
        UUID id,
        String questionCode,
        String question,
        String answer,
        String module,    // Module name
        UUID moduleId,     // Module ID
        String moduleFullName, // Add fullName here
        String imagePath,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}

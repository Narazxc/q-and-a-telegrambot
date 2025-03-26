package com.example.telegrambot.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QAndADTO(

        @NotBlank(message = "Question code cannot be empty")
        String questionCode,

        @NotBlank(message = "Message cannot be empty")
        String question,

        @NotBlank(message = "Answer cannot be empty")
        String answer,

        @NotNull(message = "Module id cannot be empty")
        UUID moduleId // Keep it as Long since it references an ID
) {
}

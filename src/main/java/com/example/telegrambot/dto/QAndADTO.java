package com.example.telegrambot.dto;

import com.example.telegrambot.model.QAndAModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record QAndADTO(

//        , groups = QAndAModel.class
        @NotBlank(message = "Question code cannot be empty")
        String questionCode,

        @NotBlank(message = "Message cannot be empty")
        String question,

        @NotBlank(message = "Answer cannot be empty")
        String answer,

        String fullName,

        @NotNull(message = "Module id cannot be empty")
        UUID moduleId // Keep it as Long since it references an ID
) {
}

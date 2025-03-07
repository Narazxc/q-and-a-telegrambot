package com.example.telegrambot.Exception.QAndA;

import java.util.UUID;

public class QAndANotFoundException extends RuntimeException {
    public QAndANotFoundException(UUID id) {
        super("Q And A with ID " + id + " not found");
    }
}

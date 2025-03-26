package com.example.telegrambot.exception.Module;

import java.util.UUID;

public class ModuleNotFoundException extends RuntimeException {
    public ModuleNotFoundException(UUID id) {
        super("Module with ID " + id + " not found");
    }
}

package com.example.telegrambot.Exception.Module;

public class DuplicateModuleNameException extends RuntimeException {
  public DuplicateModuleNameException(String moduleName) {
    super("Module name '" + moduleName + "' already exists. Please use a different name.");
  }
}

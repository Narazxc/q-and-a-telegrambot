package com.example.telegrambot.config;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileUploadConfig {

    @Value("${file.upload.max-size}")
    private long maxFileSize;

    @Value("${file.upload.allowed-types}")
    private String allowedTypes;

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public Set<String> getAllowedTypes() {
        return Arrays.stream(allowedTypes.split(","))
                .collect(Collectors.toSet());
    }

}

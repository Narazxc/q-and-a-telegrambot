package com.example.telegrambot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PublicFolderConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map the /uploads URL to the /root/uploads directory
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/C:\\Users\\Sitha Sovannara\\Downloads\\telegramBotPostgresUUID/uploads/"); // Ensure the full path is specified
    }
}
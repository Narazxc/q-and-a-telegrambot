package com.example.telegrambot.util;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String status;
    private T data;
    private String message;

    // Constructor for success responses
    public ApiResponse(String status, T data) {
        this.status = status;
        this.data = data;
    }

    // Constructor for error responses
    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }


    public String getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}

package com.example.telegrambot.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String status;
    private T data;
    private String message;
    private List<String> error;

//    // Constructor for success responses
//    public ApiResponse(String status, T data) {
//        this.status = status;
//        this.data = data;
//    }
//
//    // Constructor for error responses
//    public ApiResponse(String status, String message) {
//        this.status = status;
//        this.message = message;
//    }
//
//
//    public String getStatus() {
//        return status;
//    }
//
//    public T getData() {
//        return data;
//    }
//
//    public String getMessage() {
//        return message;
//    }

    // Constructor for success responses
    public ApiResponse(String status, T data) {
        this.status = status;
        this.data = data;
    }

    // Constructor for error responses with a single message
    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // ✅ Constructor for error responses with multiple errors
    public ApiResponse(String status, List<String> error) {
        this.status = status;
        this.error = error;
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

    public List<String> getError() { // ✅ Getter name should match the field
        return error;
    }
}

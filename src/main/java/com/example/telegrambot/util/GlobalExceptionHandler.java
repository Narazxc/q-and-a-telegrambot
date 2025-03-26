package com.example.telegrambot.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.telegrambot.exception.Module.DuplicateModuleNameException;
import com.example.telegrambot.exception.Module.ModuleNotFoundException;
import com.example.telegrambot.exception.QAndA.DuplicateQAndACodeException;
import com.example.telegrambot.exception.QAndA.QAndANotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<ApiResponse<String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
//        logger.error("Data integrity violation: {}", ex.getMessage());
//        String errorMessage = "The question code already exists. Please use a unique code.";
//        return ResponseEntity.status(HttpStatus.CONFLICT)
//                .body(new ApiResponse<>("Error", errorMessage));
//    }
    // Handle Duplicate Key (Unique Constraint) Errors
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleDuplicateKeyException(DataIntegrityViolationException ex) {
        ApiResponse<String> response = new ApiResponse<>("error", "This value already exists. Please use a different one.");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // 409 Conflict
    }

    @ExceptionHandler(DuplicateModuleNameException.class)
    public ResponseEntity<ApiResponse<String>> handleDuplicateModuleNameException(DuplicateModuleNameException ex) {
        ApiResponse<String> response = new ApiResponse<>("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // 409 Conflict
    }

    @ExceptionHandler(ModuleNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleModuleNotFoundException(ModuleNotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

//    Q And A
    @ExceptionHandler(DuplicateQAndACodeException.class)
    public ResponseEntity<ApiResponse<String>> handleDuplicateQAndACodeException(DuplicateQAndACodeException ex) {
        ApiResponse<String> response = new ApiResponse<>("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(QAndANotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleQAndANotFoundException(QAndANotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    // Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Collect all validation error messages
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // Return a 400 Bad Request with the validation errors inside ApiResponse
        ApiResponse<Map<String, String>> response = new ApiResponse<>("Validation Error", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}


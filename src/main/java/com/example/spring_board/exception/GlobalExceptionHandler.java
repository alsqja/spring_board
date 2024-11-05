package com.example.spring_board.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", ex.getStatusCode().value());
        errorDetails.put("error", ex.getStatusCode());
        errorDetails.put("message", ex.getReason());
        errorDetails.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(errorDetails, ex.getStatusCode());
    }
}

package ru.kata.spring.boot_security.demo.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        String status,
        String message,
        LocalDateTime timestamp

) {
    public static ErrorResponse of(String status, String message) {
        return new ErrorResponse(status, message, LocalDateTime.now());
    }
}

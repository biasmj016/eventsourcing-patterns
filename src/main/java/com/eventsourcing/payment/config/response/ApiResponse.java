package com.eventsourcing.payment.config.response;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        String status,
        String message,
        LocalDateTime timestamp
) {
    public ApiResponse(String status, String message) {
        this(status, message, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>("success", null);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>("fail", message);
    }
}
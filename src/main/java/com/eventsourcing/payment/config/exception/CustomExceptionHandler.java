package com.eventsourcing.payment.config.exception;

import com.eventsourcing.payment.config.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.eventsourcing.payment.config.response.ApiResponse.fail;

@RestControllerAdvice
public class CustomExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFoundException(NoSuchElementException ex) {
        logError(ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(fail("Requested resource not found: " + ex.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ApiResponse<String>> handleBadRequestException(RuntimeException ex) {
        logError(ex);
        return ResponseEntity.badRequest().body(fail("Invalid request: " + ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Invalid request parameters");

        logError(ex);
        return ResponseEntity.badRequest().body(fail(errorMessage));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiResponse<String>> handleSQLException(SQLException ex) {
        String errorMessage = "Database error (SQL State: " + ex.getSQLState() + "): " + ex.getMessage();
        logError(ex);
        return ResponseEntity.internalServerError().body(fail(errorMessage));
    }

    private void logError(Exception ex) {
        logger.error("[{}] {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
    }
}
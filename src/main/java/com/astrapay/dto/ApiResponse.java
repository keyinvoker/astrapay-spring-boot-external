package com.astrapay.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.<T>builder()
                .code(200)
                .message("Success")
                .data(data)
                .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(ApiResponse.<T>builder()
                .code(200)
                .message(message)
                .data(data)
                .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(int code, String message) {
        return ResponseEntity.status(code)
                .body(ApiResponse.<T>builder()
                        .code(code)
                        .message(message)
                        .data(null)
                        .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<T>builder()
                        .code(404)
                        .message(message)
                        .data(null)
                        .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> conflict(String message) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.<T>builder()
                        .code(409)
                        .message(message)
                        .data(null)
                        .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> internalServerError(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.<T>builder()
                        .code(500)
                        .message(message)
                        .data(null)
                        .build());
    }
}
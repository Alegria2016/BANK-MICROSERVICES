package com.fal.client_service.application.dto;


import java.time.Instant;

public record ApiResponse<T>(
        Integer status,
        String message,
        T data,
        Instant timestamp,
        String path
) {
    public Integer getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public Instant getTimestamp() { return timestamp; }
    public String getPath() { return path; }
}

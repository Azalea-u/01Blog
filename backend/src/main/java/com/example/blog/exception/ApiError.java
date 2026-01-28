package com.example.blog.exception;

import java.time.OffsetDateTime;

public class ApiError {

    private int status;
    private String error;
    private String message;
    private OffsetDateTime timestamp = OffsetDateTime.now();

    public ApiError(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public OffsetDateTime getTimestamp() { return timestamp; }
}

package com.jesussoto.sparklewebservice.model;

public class ErrorResponseBody {

    long timestamp;
    String error;
    int status;
    String exception;
    String message;

    public ErrorResponseBody(long timestamp, String error, int status, String exception, String message) {
        this.timestamp = timestamp;
        this.error = error;
        this.status = status;
        this.exception = exception;
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
